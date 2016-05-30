import request from 'superagent';
import { createSelector } from 'reselect';
import { normalizeTag, normalizeTags } from 'utils/normalize';

import * as Consts from './common';

// Constants

const FETCH_TAGS_FINISHED = 'tags/FETCH_TAGS_FINISHED';
const FETCH_TAGS_REQUEST = 'tags/FETCH_TAGS_REQUEST';
const FETCH_TAG_REQUEST = 'tags/FETCH_TAG_REQUEST';
const LOAD_TAG = 'tags/LOAD_TAG';

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

  case FETCH_TAG_REQUEST:
    return {
      ...state,
      tags: {
        ...state.tags,
        [action.id]: {
          ...state.tags[action.id],
          $loading: true,
        },
      },
    };

  case LOAD_TAG:
    return {
      ...state,
      tags: {
        ...state.tags,
        [action.tag.id]: action.tag,
      },
    };

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

      // Pagination information
      const totalItems = +(res.headers['x-pagination-total'] || 0);
      const perPage = +(res.headers['x-pagination-limit'] || 20);

      // Normalize the response.
      const norm = normalizeTags(res.body);
      dispatch({
        type: Consts.UPDATE_TAGS,
        tags: norm.entities.tags || {},
        current: norm.result,

        pageNumber: page,
        maxPages: Math.ceil(totalItems / perPage),
      });
    });
  };
}

export function fetchTag(id) {
  return (dispatch) => {
    dispatch({ type: FETCH_TAG_REQUEST, id });

    request
    .get('/api/tags/' + id)
    .end(function (err, res) {
      if (err) {
        dispatch({ type: Consts.REQUEST_ERROR, error: err });
        return;
      }

      // Normalize the response and only pull out the tag.
      const norm = normalizeTag(res.body);
      const tag = norm.entities.tags[id];

      dispatch({
        type: LOAD_TAG,
        tag: tag || {},
      });
    });
  };
}

// Selectors

export const currentTagsSelector = (state) => {
  const tagIds = state.tags.current;
  return tagIds.map(i => state.tags.tags[i]);
};
