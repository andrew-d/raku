import request from 'utils/superagent';
import { normalizeTag, normalizeTags } from 'utils/normalize';
import { FETCH_TAGS_REQUEST, FETCH_TAGS_SUCCESS, FETCH_TAGS_FAILURE } from 'constants';


export function fetchTags() {
  return {
    types: [FETCH_TAGS_REQUEST, FETCH_TAGS_SUCCESS, FETCH_TAGS_FAILURE],
    promise: () => request.get('/api/tags').end(),
  };
}


// Action creators
// TODO: maybe we want a 'current tags page' in the state, and then have a
// selector from there.  our 'fetch tags' can fetch a page of tags by number,
// and then update the tag descriptions in the store, and the IDs of the
// current 'page' of tags.
