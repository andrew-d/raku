import request from 'superagent';
import { normalizeDocument, normalizeDocuments } from 'utils/normalize';

import * as Consts from './common';

// Constants

const FETCH_DOCUMENTS_REQUEST = 'documents/FETCH_DOCUMENTS_REQUEST';
const FETCH_DOCUMENTS_FINISHED = 'documents/FETCH_DOCUMENTS_FINISHED';

// Reducer

const initialState = {
  // Metadata
  $loading: false,

  // Mapping of document ID --> document object
  documents: {},

  // IDs of documents in the current page
  current: [],

  // Pagination information
  pageNumber: 1,
  maxPages: 1,
};

export default function reducer(state = initialState, action = {}) {
  switch (action.type) {
  case FETCH_DOCUMENTS_REQUEST:
    return { ...state, $loading: true };

  case FETCH_DOCUMENTS_FINISHED:
    return { ...state, $loading: false };

  case Consts.UPDATE_DOCUMENTS:
    const documents = Object.assign({}, state.documents, action.documents);
    return {
      ...state,
      documents,
      current: action.current,
      pageNumber: action.pageNumber || 1,
      maxPages: action.maxPages || 1,
    };

  default:
    return state;
  }
}

// Action Creators

export function fetchDocuments(page = 1) {
  return (dispatch) => {
    dispatch({ type: FETCH_DOCUMENTS_REQUEST });

    request
    .get('/api/documents')
    .query({ page })
    .end(function (err, res) {
      dispatch({ type: FETCH_DOCUMENTS_FINISHED });

      if (err) {
        dispatch({ type: Consts.REQUEST_ERROR, error: err });
        return;
      }

      // Normalize the response and only pull out the document bodies.
      const norm = normalizeDocuments(res.body.data);
      dispatch({
        type: Consts.UPDATE_DOCUMENTS,
        documents: norm.entities.documents,
        current: norm.result,

        pageNumber: page,

        // TODO: use proper per-page
        maxPages: Math.ceil(res.body.meta.count / 20),
      });
    });
  };
}

// Selectors

export const currentDocumentsSelector = (state) => {
  const documentIds = state.documents.current;
  return documentIds.map(i => state.documents.documents[i]);
};
