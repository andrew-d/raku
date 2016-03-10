import request from 'superagent';
import { normalizeTag, normalizeTags } from 'utils/normalize';

import { UPDATE_DOCUMENT, UPDATE_TAGS, REQUEST_ERROR } from './common';

// Constants

const FETCH_TAGS_REQUEST = 'tags/FETCH_TAGS_REQUEST';

// Reducer

const initialState = {
  tags: {},
};

export default function reducer(state = initialState, action = {}) {
  switch (action.type) {
  case UPDATE_TAGS:
    const tags = Object.assign({}, state.tags, action.tags);

    return { ...state, tags };

  default:
    return state;
  }
}

// Action Creators

export function fetchTags() {
  return (dispatch) => {
    dispatch({ type: FETCH_TAGS_REQUEST });

    request
    .get('/api/tags')
    .end(function (err, res) {
      if (err) {
        dispatch({ type: REQUEST_ERROR, error: err });
        return;
      }

      // Normalize the response and only pull out the tag bodies.
      const norm = normalizeTags(res.body);

      dispatch({ type: UPDATE_TAGS, tags: norm.entites.tags });
    });
  };
}
