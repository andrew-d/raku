/* make-webpack-config.js
 *
 * This file is responsible for building a webpack configuration (usually found
 * in `webpack.config.js`) from a set of options.  Since we have several
 * different configurations - development vs. production, whether hot reloading
 * is enabled, and so on, this file takes some input options and then outputs
 * the appropriate configuration.
 */

var autoprefixer = require('autoprefixer'),
    csswring = require('csswring'),
    path = require('path'),
    webpack = require('webpack'),
    ExtractTextPlugin = require('extract-text-webpack-plugin'),
    HtmlWebpackPlugin = require('html-webpack-plugin');

// List of valid options you can pass.  Unknown options will throw.
var VALID_OPTIONS = {
  'production'   : Boolean,
  'hotReload'    : Boolean,
  'lint'         : Boolean,
  'devtool'      : String,
  'assetsPath'   : String,
  'devServerAddr': String,
};

// ------------------------------------------------------------

/**
 * Validates the input options and returns the new options object.
 *
 * @param {Object} options
 * @returns {Number} The validated options
 */
var validateOptions = function(options) {
  var out = {};

  Object.keys(options).forEach(function(key) {
    // If this isn't an option, throw.
    if (!VALID_OPTIONS[key]) {
      throw new Error("make-webpack-config: option '" + key + "' doesn't exist");
    }

    // Coerce
    out[key] = VALID_OPTIONS[key](options[key]);

    // Explicitly check for NaN.
    if (Number.isNaN(out[key])) {
      throw new Error("make-webpack-config: option '" + key + "' evaluated to NaN");
    }
  });

  // Set default options.
  if (out.production === undefined) {
    out.production = false;
  }
  if (out.hotReload === undefined) {
    out.hotReload = !out.production;
  }
  if (out.lint === undefined) {
    out.lint = true;
  }
  if (out.devtool === undefined) {
    out.devtool = out.production ? null : 'source-map';
  }
  if (out.assetsPath === undefined) {
    out.assetsPath = 'assets';
  }
  if (out.devServerAddr === undefined) {
    out.devServerAddr = 'localhost:3000';
  }

  return out;
};


/**
 * Sets the appropriate Javascript-related options in the configuration, based
 * on the input options.
 *
 * @param {Object} config The Webpack configuration object
 * @param {Object} opts The validated input options
 */
function buildJavascriptConfig(config, opts) {
  // 1. Make the appropriate loaders
  // --------------------------------------------------
  var jsLoaders = ["babel-loader"];
  if (opts.hotReload) {
    // Must go before other loaders.
    jsLoaders.unshift('react-hot-loader');
  }

  Array.prototype.push.apply(config.module.loaders, [
    { test: /\.js$/,   loaders: jsLoaders, exclude: /node_modules/ },
    { test: /\.jsx$/,  loaders: jsLoaders, exclude: /node_modules/ },
    { test: /\.json$/, loaders: ['json-loader'] },
  ]);

  // 2. Add plugins for Javascript
  // --------------------------------------------------
  var extraPlugins = opts.production ? [
    new webpack.DefinePlugin({
      "process.env": {
        NODE_ENV:   JSON.stringify("production"),
        NODE_DEBUG: JSON.stringify(""),
      }
    }),
    new webpack.optimize.OccurenceOrderPlugin(/* preferEntry = */ true),
    new webpack.optimize.UglifyJsPlugin({
      compressor: {
        warnings: false,
      },
    }),
    new webpack.optimize.DedupePlugin(),
  ] : [
    new webpack.HotModuleReplacementPlugin(),
    new webpack.DefinePlugin({
      "process.env": {
        NODE_ENV:   JSON.stringify(process.env.NODE_ENV || ""),
        NODE_DEBUG: JSON.stringify(process.env.NODE_DEBUG || ""),
      }
    }),
  ];

  Array.prototype.push.apply(config.plugins, extraPlugins);

  // 3. Add entry points for react-hot-loader
  // --------------------------------------------------
  if (opts.hotReload) {
    Array.prototype.unshift.apply(config.entry, [
      'webpack-dev-server/client?http://' + opts.devServerAddr,
      'webpack/hot/only-dev-server'
    ]);
  }
}


// Helper function for below
function extractInProduction(loaders) {
  return ExtractTextPlugin.extract(loaders.substr(loaders.indexOf('!')))
}

/**
 * Sets the appropriate style-related options in the configuration, based
 * on the input options.
 *
 * @param {Object} config The Webpack configuration object
 * @param {Object} opts The validated input options
 */
function buildStyleConfig(config, opts) {
  // 1. Make the appropriate loaders
  // --------------------------------------------------
  var cssLoaders = [
    'style-loader',

    // We apply postcss-loader to files that are imported, in addition to
    // our files.
    'css-loader?importLoaders=1',

    // Autoprefixer and minimizer
    'postcss-loader',
  ];

  // Set postcss settings
  config.postcss = [
    autoprefixer({ browsers: ['last 3 versions', 'safari 5', 'ie 9', 'ios 6', 'android 4'] }),
  ];
  if (opts.production) {
    // Only minimize CSS in production.
    config.postcss.push(csswring);
  }

  var sassOptions = [
    'precision=10',
    'includePaths[]=' + path.resolve(__dirname, '..', 'node_modules'),
  ];

  var sassLoaders = cssLoaders.concat('sass-loader?' + sassOptions.join('&'));

  // Join the loaders together now.
  cssLoaders = cssLoaders.join('!');
  sassLoaders = sassLoaders.join('!');

  // In production, we want to run these loaders through the ExtractTextPlugin.
  // This involves stripping off the 'style' loader and calling the .extract()
  // function.
  if (opts.production) {
    cssLoaders = extractInProduction(cssLoaders);
    sassLoaders = extractInProduction(sassLoaders);
  }

  Array.prototype.push.apply(config.module.loaders, [
    { test: /\.css$/,  loader: cssLoaders },
    { test: /\.scss$/, loader: sassLoaders },
  ]);

  // 2. Add plugins for styles
  // --------------------------------------------------
  Array.prototype.push.apply(config.plugins, [
    new ExtractTextPlugin(
      path.join(opts.assetsPath, "styles-[hash].css"),
      { allChunks: true }
    )
  ]);
}


/**
 * Sets the appropriate static file-related options in the configuration, based
 * on the input options.
 *
 * @param {Object} config The Webpack configuration object
 * @param {Object} opts The validated input options
 */
function buildStaticConfig(config, opts) {
  // 1. Build the options.
  // --------------------------------------------------
  var fileLoaderOptions = { name: path.join(opts.assetsPath, '[hash].[ext]') },
      urlLoaderOptions = Object.assign({}, fileLoaderOptions, {
        limit: 10000,
      });

  // 2. Make the appropriate loaders
  // --------------------------------------------------
  Array.prototype.push.apply(config.module.loaders, [
    // Various font types from Bootstrap
    {
      test: /\.(woff|woff2)(\?.*)?$/,
      loader: "url-loader",
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'application/font-woff' }),
    },
    {
      test: /\.ttf(\?.*)?$/,
      loader: "file-loader",
      query: Object.assign({}, fileLoaderOptions, { mimetype: 'application/vnd.ms-fontobject' }),
    },
    {
      test: /\.eot(\?.*)?$/,
      loader: "file-loader",
      query: Object.assign({}, fileLoaderOptions, { mimetype: 'application/x-font-ttf' }),
    },
    {
      test: /\.svg(\?.*)?$/,
      loader: "file-loader",
      query: Object.assign({}, fileLoaderOptions, { mimetype: 'image/svg+xml' }),
    },

    // Inline base64 URLs for small images, direct URLs for the rest
    {
      test: /\.png$/,
      loader: "url-loader",
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'image/png' }),
    },
    {
      test: /\.gif$/,
      loader: "url-loader",
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'image/gif' }),
    },
    {
      test: /\.(jpg|jpeg)$/,
      loader: "url-loader",
      query: Object.assign({}, urlLoaderOptions, { mimetype: 'image/jpg' }),
    },
  ]);
}


/**
 * Builds the configuration from the given options.
 *
 * @param {Object} opts The input options
 * @returns {Object} The built configuration
 */
module.exports = function(opts) {
  var options = validateOptions(opts);

  // Default configuration.
  var config = {
    entry:   [path.join(__dirname, '..', 'app', 'index')],
    target:  'web',
    debug:   !options.production,
    devtool: options.devtool,

    // Place assets into './dist' and add cache-busting if this is a production
    // build.
    output: {
      path:       path.join(__dirname, '..', options.production ? 'dist' : 'build'),
      publicPath: '/',
      filename:   path.join(options.assetsPath , options.production ? 'bundle-[hash].js' : 'bundle.js'),
    },

    resolve: {
      modulesDirectories: ['node_modules', 'app'],
      extensions: ['', '.js', '.jsx', '.json'],
    },

    resolveLoader: {
      alias: {
        // This is an alias that allows us to copy files from our app directory
        // to the output directory as-is; this is useful for static files.
        'copy': 'file-loader?name=[path][name].[ext]&context=' + path.join(__dirname, '..', 'app'),
      },
    },

    module: {
      // Lint only when specified
      preLoaders: options.lint ? [
        {
          test: /\.jsx?$/,
          exclude: /node_modules/,
          loaders: ['eslint-loader'],
        },
      ] : [],

      // These are added below.
      loaders: [],
    },

    // Common plugins.  More are added below, depending on options.
    plugins: [
      // Update index.html with the appropriate file names.
      new HtmlWebpackPlugin({
        template: path.join('app', 'index.html'),
      }),

      // Prevent any assets from being emitted if there's an error.
      new webpack.NoErrorsPlugin(),

      // Allow using jquery without explicitly requiring it.
      new webpack.ProvidePlugin({
        jQuery: 'jquery',
        $: 'jquery',
      }),
    ],
  };

  // Update the configuration based on options.
  buildJavascriptConfig(config, options);
  buildStyleConfig(config, options);
  buildStaticConfig(config, options);

  return config;
};
