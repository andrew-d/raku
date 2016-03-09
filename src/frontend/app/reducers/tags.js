import request from superagent;
import { normalizeTag, normalizeTags } from 'util/normalize';


// Constants

const FETCH_TAGS_REQUEST = 'tags/FETCH_TAGS_REQUEST';
const FETCH_TAGS_SUCCESS = 'tags/FETCH_TAGS_SUCCESS';
const FETCH_TAGS_FAILURE = 'tags/FETCH_TAGS_FAILURE';

export const UPDATE_TAGS = 'tags/UPDATE_TAGS';

// Reducer

const initialState = {
  tags: {},
};

export default function reducer(state = initialState, action = {}) {
  switch (action.type) {
    default:
      return state;
  }
}

// Action creators

export function fetchTags() {
  return (dispatch, getState) => {
    dispatch({ type: FETCH_TAGS_REQUEST });

    request
      .get('/api/tags')
      .end(function (err, resp) {
        if (err) {
          dispatch({ type: FETCH_TAGS_FAILURE, error: err });
          return;
        }

        // Notify that the request succeeded
        dispatch({ type: FETCH_TAGS_SUCCESS });

        // Normalize the results
        const norm = normalizeTags(resp.body);

        // Dispatch with the corresponding tags.
        dispatch({ type: UPDATE_TAGS, tags: norm.entities.tags });
      });
  };
}

// TODO: maybe we want a 'current tags page' in the state, and then have a
// selector from there.  our 'fetch tags' can fetch a page of tags by number,
// and then update the tag descriptions in the store, and the IDs of the
// current 'page' of tags.
