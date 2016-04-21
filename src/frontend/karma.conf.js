var argv = require('yargs').argv;
var path = require('path');
var webpackConfig = require('./webpack.config.js');


var testsDir = path.resolve('./tests');
var singleRun = !argv.watch;
var coverage = process.env.TEST_COVERAGE === 'true';


var karmaConfig = {
  browsers: ['Chrome'],

  singleRun: singleRun,
  frameworks: ['mocha'],
  reporters: ['mocha'],

  basePath: path.resolve('.'),
  files: [
    {
      pattern: `${testsDir}/test-bundler.js`,
      watched: false,
      served: true,
      included: true,
    },
  ],

  preprocessors: {
    [`${testsDir}/test-bundler.js`]: ['webpack'],
  },

  webpackMiddleware: {
    noInfo: true,
  },

  plugins: [
    'karma-chrome-launcher',
    'karma-coverage',
    'karma-mocha',
    'karma-mocha-reporter',
    'karma-webpack-with-fast-source-maps',
  ],

  webpack: {
    devtool: 'cheap-module-source-map',
    resolve: Object.assign({}, webpackConfig.resolve, {
      alias: {
        sinon: 'sinon/pkg/sinon.js',
      },
    }),

    plugins: webpackConfig.plugins,
    module: {
      noParse: [
        /\/sinon\.js/
      ],
      loaders: webpackConfig.module.loaders.concat([
         {
           test: /sinon(\\|\/)pkg(\\|\/)sinon\.js/,
           loader: 'imports?define=>false,require=>false',
         },
      ]),
    },

    externals: Object.assign({}, webpackConfig.externals, {
      'react/lib/ExecutionEnvironment': true,
      'react/lib/ReactContext': 'window',
      'text-encoding': 'window',
    }),
  },
};


if (coverage) {
  karmaConfig.reporters.push('coverage');
  karmaConfig.webpack.module.preLoaders = [{
    test: /\.(js|jsx)$/,
    include: 'app',
    loader: 'isparta-loader',
    exclude: /node_modules/,
  }];
}


module.exports = function(cfg) {
  cfg.set(karmaConfig);
};
