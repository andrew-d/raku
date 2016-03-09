// This must come first, in this order
import 'ie8';
import 'html5shiv/dist/html5shiv';
import 'html5shiv/dist/html5shiv-printshiv';
import 'babel-polyfill';

import React from 'react';
import ReactDOM from 'react-dom';
import { Router, browserHistory } from 'react-router';

// Import vendor styles here.
import 'bootstrap-sass/assets/stylesheets/_bootstrap.scss';

// Static files
import 'copy!favicon.ico';
import 'copy!robots.txt';

import routes from './routes';
ReactDOM.render(
  <Router history={browserHistory}>
    {routes}
  </Router>,
  document.getElementById('app')
);
