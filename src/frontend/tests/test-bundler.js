import chai from 'chai';
import chaiEnzyme from 'chai-enzyme';

chai.use(chaiEnzyme());

global.chai = chai;
global.expect = chai.expect;
global.should = chai.should();

// For use with karma-webpack-with-fast-source-maps
const __karmaWebpackManifest__ = [];
const inManifest = (path) => __karmaWebpackManifest__.indexOf(path) !== -1;

// Require all `tests/**/*.spec.js`
const testsContext = require.context('./', true, /\.spec\.js$/);

// Only run tests that have changed after the first pass.
const testsToRun = testsContext.keys().filter(inManifest);
(testsToRun.length ? testsToRun : testsContext.keys()).forEach(testsContext);

// Require all `app/**/*.js` except for `main.js` (for isparta coverage reporting)
const componentsContext = require.context('../app/', true, /^((?!main).)*\.js$/);
componentsContext.keys().forEach(componentsContext);
