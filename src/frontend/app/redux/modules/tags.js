import request from 'superagent';
import { normalizeTag, normalizeTags } from 'utils/normalize';

import * as Consts from './common';

// Reducer

const initialState = {
  $loading: false,
  tags: {},
};

export default function reducer(state = initialState, action = {}) {
  switch (action.type) {
  case Consts.REQUEST_STARTED:
    return { ...state, $loading: true };

  case Consts.REQUEST_FINISHED:
    return { ...state, $loading: false };

  case Consts.UPDATE_TAGS:
    const tags = Object.assign({}, state.tags, action.tags);
    return { ...state, tags };

  default:
    return state;
  }
}

// Action Creators

export function fetchTags() {
  return (dispatch) => {
    dispatch({ type: Consts.REQUEST_STARTED });

    request
    .get('/api/tags')
    .end(function (err, res) {
      dispatch({ type: Consts.REQUEST_FINISHED });

      if (err) {
        dispatch({ type: Consts.REQUEST_ERROR, error: err });
        return;
      }

      // Normalize the response and only pull out the tag bodies.
      const norm = normalizeTags(res.body);
      dispatch({ type: Consts.UPDATE_TAGS, tags: norm.entities.tags });
    });
  };
}
