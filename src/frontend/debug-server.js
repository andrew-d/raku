#!/usr/bin/env node

var morgan = require('morgan'),
    nomnom = require('nomnom'),
    url = require('url'),
    webpack = require('webpack'),
    WebpackDevServer = require('webpack-dev-server');

// Silly helper
function endsWith(str, suffix) {
  return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

// ----------------------------------------------------------------------
// Parse command-line options
var opts = nomnom
  .option('host', {
    abbr: 'h',
    default: 'localhost',
    help: 'Host to listen on',
  })
  .option('port', {
    abbr: 'p',
    default: 3001,
    help: 'Port to listen on',
    callback: function(port) {
      var parsed = parseInt(port);

      if (port != parsed) {
        return "port must be an integer";
      }

      if (port <= 0 || port > 65535) {
        return "port must be in the range 1-65535";
      }
    },
  })
  .option('upstream', {
    default: null,
    help: "If specified, proxy all requests matching 'upstream-prefix' here",
    callback: function(upstream) {
      if (!/^https?\:\/\//i.test(upstream)) {
        return 'upstream must start with "http://" or "https://"';
      }
    },
  })
  .option('upstream-prefix', {
    default: '/api',
    help: 'Any request matching this prefix gets proxied upstream',
  })
  .parse();

// ----------------------------------------------------------------------
// Build configuration
var webpackConfig = require('./conf/make-webpack-config.js')({
  production   : false,
  hotReload    : true,
  lint         : false,
  devServerAddr: opts.host + ':' + opts.port,
});

var devServerConfig = {
  publicPath: webpackConfig.output.publicPath,
  hot: true,
  historyApiFallback: true,

  stats: {
    colors: true,
    hash: true,
    timings: true,
    assets: true,
    chunks: true,

    // Hide some verbose information that we don't need.
    modules: false,
    cached: false,
  },

  proxy: {},
};

// Configure proxy
var prefixPath;
if (opts.upstream) {
  var logger = morgan('dev');

  // Parse our upstream prefix using the URL module and ensure it ends with
  // '/*' so proxying works.
  var prefixUrl = url.parse(opts['upstream-prefix']);

  prefixPath = prefixUrl.pathname;
  if (endsWith(prefixPath, '/*')) {
    // Do nothing
  } else if (endsWith(prefixPath, '/')) {
    prefixPath += '*';
  } else {
    prefixPath += '/*';
  }

  // Set up the proxy configuration.
  devServerConfig.proxy[prefixPath] = {
    target: opts.upstream,
    changeOrigin: true,
    ws: true,
    configure: function(proxy) {
      // The configure function is called on every request, so don't add a
      // listener each time.
      if (proxy.__has_logger) return;

      proxy.on('proxyReq', function(proxyReq, req, res, options) {
        logger(req, res, function() {});
      });

      proxy.__has_logger = true;
    },
  };
}


// ----------------------------------------------------------------------
// Start dev server.
new WebpackDevServer(webpack(webpackConfig), devServerConfig).listen(
  opts.port, opts.host,
  function(err, result) {
    if (err) {
      console.log(err);
    }

    console.log('Listening at http://' + opts.host + ':' + opts.port);
    if (opts.upstream) {
      console.log('Proxying requests that match "' + prefixPath + '" to: ' + opts.upstream);
    }

    console.log('--------------------------------------------------');
  });
