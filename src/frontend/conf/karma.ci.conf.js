var karmaFactory = require('./make-karma-config.js');

module.exports = function(config) {
  config.set(karmaFactory({
    ci:       true,
    coverage: true,
    notify:   false,
  }));
};
