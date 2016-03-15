import request from 'superagent';
import { createSelector } from 'reselect';
import { normalizeTag, normalizeTags } from 'utils/normalize';

import * as Consts from './common';

// Constants

const FETCH_TAGS_REQUEST = 'tags/FETCH_TAGS_REQUEST';
const FETCH_TAGS_FINISHED = 'tags/FETCH_TAGS_REQUEST';

// Reducer

const initialState = {
  // Metadata
  $loading: false,

  // Mapping of tag ID --> tag object
  tags: {},

  // IDs of tags in the current page
  current: [],

  // Pagination information
  pageNumber: 1,
  maxPages: 3,
};

export default function reducer(state = initialState, action = {}) {
  switch (action.type) {
  case FETCH_TAGS_REQUEST:
    return { ...state, $loading: true };

  case FETCH_TAGS_FINISHED:
    return { ...state, $loading: false };

  case Consts.UPDATE_TAGS:
    const tags = Object.assign({}, state.tags, action.tags);
    return {
      ...state,
      tags,
      current: action.current,
      pageNumber: action.pageNumber || 1,
      maxPages: action.maxPages || 1,
    };

  default:
    return state;
  }
}

// Action Creators

export function fetchTags(page = 1) {
  return (dispatch) => {
    dispatch({ type: FETCH_TAGS_REQUEST });

    request
    .get('/api/tags')
    .query({ page })
    .end(function (err, res) {
      dispatch({ type: FETCH_TAGS_FINISHED });

      if (err) {
        dispatch({ type: Consts.REQUEST_ERROR, error: err });
        return;
      }

      // Normalize the response.
      const norm = normalizeTags(res.body.data);
      dispatch({
        type: Consts.UPDATE_TAGS,
        tags: norm.entities.tags,
        current: norm.result,

        pageNumber: page,

        // TODO: use proper per-page
        maxPages: Math.ceil(res.body.meta.count / 20),
      });
    });
  };
}

// Selectors

export const currentTagsSelector = (state) => {
  const tagIds = state.tags.current;
  return tagIds.map(i => state.tags.tags[i]);
};
