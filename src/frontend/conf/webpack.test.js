module.exports = require('./make-webpack-config')({
  production   : false,
  hotReload    : false,
  lint         : true,
  devtool      : 'inline-source-map',
});
