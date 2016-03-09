module.exports = require('./make-webpack-config')({
  production   : false,
  lint         : true,
  devtool      : 'source-map',
  devServerAddr: 'localhost:3000',
});
