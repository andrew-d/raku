// This must come first, in this order
import 'ie8';
import 'html5shiv/dist/html5shiv';
import 'html5shiv/dist/html5shiv-printshiv';
import 'babel-polyfill';

import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { Router } from 'react-router';

// Import vendor styles here.
import 'bootstrap-sass/assets/stylesheets/_bootstrap.scss';

// Static files
import 'copy!favicon.ico';
import 'copy!robots.txt';

import createStoreAndHistory from './redux/store';
import routes from './routes';


const [store, history] = createStoreAndHistory();

ReactDOM.render(
  <Provider store={store}>
    <Router history={history}>
      {routes}
    </Router>
  </Provider>,
  document.getElementById('app')
);
