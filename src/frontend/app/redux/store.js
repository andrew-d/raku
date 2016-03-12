import thunkMiddleware from 'redux-thunk';
import createLogger from 'redux-logger';
import { routerReducer, syncHistoryWithStore } from 'react-router-redux';
import { createStore, combineReducers, compose, applyMiddleware } from 'redux';
import { browserHistory } from 'react-router';

import documents from './modules/documents';
import tags from './modules/tags';


export default function createStoreAndHistory() {
  // Create enhancer that we pass to createStore()
  const middleware = [
    thunkMiddleware,
    createLogger(),
  ];
  let enhancer = applyMiddleware.apply(null, middleware);

  if (process.env.NODE_ENV !== 'production') {
    // Select the Redux-Devtools Chrome extension if it exists, otherwise
    // fallback to our devtools component.
    const devTools = window.devToolsExtension
      ? window.devToolsExtension()
      : require('components/DevTools').default.instrument();

    enhancer = compose(enhancer, devTools);
  }

  // Combine all reducers into the root one.
  const reducers = combineReducers({
    // App reducers
    documents,
    tags,

    // react-router-redux
    routing: routerReducer,
  });

  // Create store
  const store = createStore(reducers, {}, enhancer);
  const history = syncHistoryWithStore(browserHistory, store);
  return [store, history];
}
