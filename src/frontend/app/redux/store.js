import thunkMiddleware from 'redux-thunk';
import createLogger from 'redux-logger';
import { syncHistoryWithStore } from 'react-router-redux';
import { createStore, compose, applyMiddleware } from 'redux';
import { browserHistory } from 'react-router';


export default function createStoreAndHistory() {
  // Create enhancer that we pass to createStore()
  const middleware = [
    thunkMiddleware,
    createLogger(),
  ];
  let enhancer = applyMiddleware.apply(null, middleware);

  if (__DEV__) {
    // Select the Redux-Devtools Chrome extension if it exists, otherwise
    // fallback to our devtools component.
    const devTools = window.devToolsExtension
      ? window.devToolsExtension()
      : require('components/DevTools').default.instrument();

    enhancer = compose(enhancer, devTools);
  }

  // Pull in the reducers
  const reducers = require('./rootReducer').default;

  // Create store
  const store = createStore(reducers, {}, enhancer);

  // Support hot reloading in development.
  if (module.hot) {
    module.hot.accept('./rootReducer', () => {
      const reducers = require('./rootReducer').default;

      store.replaceReducer(reducers);
    });
  }

  const history = syncHistoryWithStore(browserHistory, store);
  return [store, history];
}
