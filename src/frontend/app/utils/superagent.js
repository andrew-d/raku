const wrapped = require('superagent-promise')(require('superagent'), Promise);
export default wrapped;
