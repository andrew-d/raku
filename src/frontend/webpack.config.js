'use strict';

var path = require('path');
var util = require('util');
var webpack = require('webpack');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');


var IS_DEVELOPMENT = (process.env.NODE_ENV === 'development');
var IS_TEST        = (process.env.NODE_ENV === 'test');
var IS_PRODUCTION  = (process.env.NODE_ENV === 'production');


/**
 * Creates the configuration for webpack-dev-server's proxy
 */
function makeProxyConfig () {
  var proxyConfig = {
    '/api/': {
      target: 'http://localhost:8080/api/',
      changeOrigin: true,
    },
  };

  return Object.keys(proxyConfig).reduce((acc, path) => {
    acc[path + '*'] = proxyConfig[path];
    acc[path + '*'].rewrite = (req) => {
      req.url = req.url.replace(path, '');
    };

    return acc;
  }, {});
}


/**
 * Returns the entry points for our application.
 */
function appEntryPoints () {
  var baseAppEntries = [
    './app/index.jsx',
  ];

  var devAppEntries = [
    'webpack-hot-middleware/client?path=/__webpack_hmr',
  ];

  return baseAppEntries
    .concat(IS_DEVELOPMENT ? devAppEntries : []);
}


/**
 * Returns the webpack plugins we're going to use.
 */
function makeWebpackPlugins () {
  var basePlugins = [
    new webpack.DefinePlugin({
      __DEV__: IS_DEVELOPMENT || IS_TEST,
      'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV),
    }),
    new ExtractTextPlugin('styles.[hash].css'),
    new HtmlWebpackPlugin({
      template: './app/index.html',
      inject: 'body',
    }),
    new CopyWebpackPlugin([
      { from: './app/favicon.ico', to: 'favicon.ico' },
      { from: './app/robots.txt', to: 'robots.txt' },
    ]),

    // Allow using jQuery without explicitly requiring it.
    new webpack.ProvidePlugin({
      jQuery: 'jquery',
      $: 'jquery',
    }),
  ];

  var notTestPlugins = [
    new webpack.optimize.CommonsChunkPlugin('vendor', '[name].[hash].js'),
  ];

  var devPlugins = [
    new webpack.NoErrorsPlugin(),
  ];

  var prodPlugins = [
    new webpack.optimize.OccurenceOrderPlugin(/* preferEntry = */ true),
    new webpack.optimize.DedupePlugin(),
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false,
      },
    }),
  ];

  return basePlugins
    .concat(IS_TEST ? [] : notTestPlugins)
    .concat(IS_PRODUCTION ? prodPlugins : [])
    .concat(IS_DEVELOPMENT ? devPlugins : []);
}


/**
 * Returns an array of loaders to handle static files.
 */
function makeStaticLoaders () {
  // 1. Build the options.
  // --------------------------------------------------
  var fileLoaderOptions = { name: path.join('assets', '[hash].[ext]') };
  var urlLoaderOptions = Object.assign({}, fileLoaderOptions, {
    limit: 10000,
  });

  // 2. Make the appropriate loaders
  // --------------------------------------------------
  return [
    // JSON files
    {
      test: /\.json$/,
      loader: 'json-loader',
    },

    // Various font types from Bootstrap
    {
      test: /\.(woff|woff2)(\?.*)?$/,
      loader: 'url-loader',
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'application/font-woff' }),
    },
    {
      test: /\.ttf(\?.*)?$/,
      loader: 'file-loader',
      query: Object.assign({}, fileLoaderOptions, { mimetype: 'application/vnd.ms-fontobject' }),
    },
    {
      test: /\.eot(\?.*)?$/,
      loader: 'file-loader',
      query: Object.assign({}, fileLoaderOptions, { mimetype: 'application/x-font-ttf' }),
    },
    {
      test: /\.svg(\?.*)?$/,
      loader: 'file-loader',
      query: Object.assign({}, fileLoaderOptions, { mimetype: 'image/svg+xml' }),
    },

    // Inline base64 URLs for small images, direct URLs for the rest
    {
      test: /\.png$/,
      loader: 'url-loader',
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'image/png' }),
    },
    {
      test: /\.gif$/,
      loader: 'url-loader',
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'image/gif' }),
    },
    {
      test: /\.(jpg|jpeg)$/,
      loader: 'url-loader',
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'image/jpg' }),
    },
  ];
}


/**
 * Creates our CSS loader(s).
 */
function makeStyleLoaders() {
  // The base thing that loads CSS
  var baseCssLoader = 'css-loader?sourceMap';

  // A regex that determines what we enable CSS modules for.
  var cssModulesRegex = /\/app\//;

  // Randomize the style names in production.
  var identName = (IS_DEVELOPMENT || IS_TEST) ?
    '[name]__[local]___[hash:base64:5]' :
    '[hash:base64]';

  // We create two loaders - one that loads things with modules, and one that loads everything else.
  var cssModulesLoader = [
    baseCssLoader,
    'modules',
    'importLoaders=1',
    'localIdentName=' + identName,
  ].join('&');

  var cssRegularLoader = [
    baseCssLoader,
    'importLoaders=1',
  ].join('&');

  var loaders = [
    // CSS modules
    {
      test: /\.css$/,
      include: cssModulesRegex,
      loaders: [
        'style-loader',
        cssModulesLoader,
        'postcss-loader',
      ],
    },

    // Everything else
    {
      test: /\.css$/,
      exclude: cssModulesRegex,
      loaders: [
        'style-loader',
        cssRegularLoader,
        'postcss-loader',
      ],
    },
  ];

  // In production, we extract the CSS to its own file.
  if (IS_PRODUCTION) {
    for (var i = 0; i < loaders.length; i++) {
      var firstLoader = loaders[i].loaders[0];
      var restLoader = loaders[i].loaders.slice(1);

      var extractingLoader = ExtractTextPlugin.extract(firstLoader, restLoader);

      loaders[i].loader = extractingLoader;
      delete loaders[i].loaders;
    }
  }

  return loaders;
}


/**
 * Creates our webpack loader list.
 */
function makeLoaders () {
  var jsDevPlugins = [
    ['react-transform', {
      transforms: [{
        transform: 'react-transform-hmr',
        imports: ['react'],
        locals: ['module'],
      }, {
        transform: 'react-transform-catch-errors',
        imports: ['react', 'redbox-react'],
      }],
    }],
  ];

  var jsProdPlugins = [
    'transform-react-remove-prop-types',
    'transform-react-constant-elements',
  ];

  var jsLoader = {
    test: /\.(js|jsx)$/,
    loader: 'babel-loader',
    exclude: /node_modules/,
    query: {
      cacheDirectory: true,
      plugins: ['transform-runtime', 'transform-decorators-legacy'],
      presets: ['es2015', 'react', 'stage-0'],
      env: {
        development: {
          plugins: jsDevPlugins,
        },
        production: {
          plugins: jsProdPlugins,
        },
      },
    },
  };

  var htmlLoader = {
    test: /\.html$/,
    loader: 'raw-loader',
    exclude: /node_modules/,
  };

  var loaders = [];
  loaders = loaders.concat(makeStaticLoaders());
  loaders = loaders.concat(makeStyleLoaders());
  loaders = loaders.concat(jsLoader);
  loaders = loaders.concat(htmlLoader);
  return loaders;
}


module.exports = {
  entry: {
    app: appEntryPoints(),
    vendor: [
      'babel-polyfill',
      'html5shiv',
      'ie8',
      'isomorphic-fetch',
      'lodash',
      'normalizr',
      'react',
      'react-bootstrap',
      'react-dom',
      'react-redux',
      'react-router',
      'react-router-bootstrap',
      'react-router-redux',
      'redux',
      'redux-logger',
      'redux-thunk',

      // Only for debug builds
      'redux-devtools',
      'redux-devtools-dock-monitor',
      'redux-devtools-log-monitor',

      // From webpack-hot-middleware
      'ansi-html',
      'html-entities',
    ],
  },

  output: {
    path: path.join(__dirname, 'dist'),
    filename: '[name].[hash].js',
    publicPath: '/',
    sourceMapFilename: '[name].[hash].js.map',
    chunkFilename: '[id].chunk.js',
  },

  devtool: 'source-map',

  resolve: {
    root: [
      path.resolve('./app'),
    ],
    extensions: ['', '.jsx', '.js', '.webpack.js', '.json'],
  },

  plugins: makeWebpackPlugins(),

  devServer: {
    outputPath: path.join(__dirname, 'dist'),
    historyApiFallback: { index: '/' },
    proxy: makeProxyConfig(),
  },

  module: {
    loaders: makeLoaders(),
  },

  postcss: function () {
    return [
      require('postcss-import')({
        addDependencyTo: webpack,
      }),
      require('postcss-url')({
        url: 'rebase',
      }),
      require('postcss-cssnext')({
        browsers: ['ie >= 8', 'last 2 versions'],
      }),
    ];
  },
};
