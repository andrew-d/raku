import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';

import documents from './modules/documents';
import tags from './modules/tags';


export default combineReducers({
  // App reducers
  documents,
  tags,

  // react-router-redux
  routing: routerReducer,
});
