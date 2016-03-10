import thunkMiddleware from 'redux-thunk';
import createLogger from 'redux-logger';
import { routerMiddleware, routerReducer } from 'react-router-redux';
import { createStore, combineReducers, compose, applyMiddleware } from 'redux';
import { browserHistory } from 'react-router';

import promiseMiddleware from './middleware/promiseMiddleware';
import tags from './reducers/tags';


export default function createStoreAndHistory() {
  const history = browserHistory;

  // Create enhancer that we pass to createStore()
  const middleware = [
    promiseMiddleware(),
    thunkMiddleware,
    createLogger(),
    routerMiddleware(history),
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
    tags,

    // react-router-redux
    routing: routerReducer,
  });

  // Create store
  const store = createStore(reducers, {}, enhancer);
  return [store, history];
}
