import request from 'superagent';
import { createSelector } from 'reselect';
import { normalizeTag, normalizeTags } from 'utils/normalize';

import * as Consts from './common';

// Reducer

const initialState = {
  // Metadata
  $loading: false,

  // Mapping of tag ID --> tag object
  tags: {},

  // Current tags page
  current: [],
  pageNumber: 1,
};

export default function reducer(state = initialState, action = {}) {
  switch (action.type) {
  case Consts.REQUEST_STARTED:
    return { ...state, $loading: true };

  case Consts.REQUEST_FINISHED:
    return { ...state, $loading: false };

  case Consts.UPDATE_TAGS:
    const tags = Object.assign({}, state.tags, action.tags);
    return {
      ...state,
      tags,
      current: action.current,
      pageNumber: action.pageNumber,
    };

  default:
    return state;
  }
}

// Action Creators

export function fetchTags(page = 1) {
  return (dispatch) => {
    dispatch({ type: Consts.REQUEST_STARTED });

    request
    .get('/api/tags')
    .query({ page })
    .end(function (err, res) {
      dispatch({ type: Consts.REQUEST_FINISHED });

      if (err) {
        dispatch({ type: Consts.REQUEST_ERROR, error: err });
        return;
      }

      // Normalize the response.
      const norm = normalizeTags(res.body);
      dispatch({
        type: Consts.UPDATE_TAGS,
        tags: norm.entities.tags,
        current: norm.result,
        pageNumber: page,
      });
    });
  };
}

// Selectors

export const currentTagsSelector = (state) => {
  const tagIds = state.tags.current;
  return tagIds.map(i => state.tags.tags[i]);
};
