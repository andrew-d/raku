import request from 'superagent';
import { normalizeDocument, normalizeDocuments } from 'utils/normalize';

import { UPDATE_DOCUMENTS, REQUEST_ERROR } from './common';

// Constants

const FETCH_DOCUMENTS_REQUEST = 'documents/FETCH_DOCUMENTS_REQUEST';

// Reducer

const initialState = {
  documents: {},
};

export default function reducer(state = initialState, action = {}) {
  switch (action.type) {
  case UPDATE_DOCUMENTS:
    const documents = Object.assign({}, state.documents, action.documents);

    return { ...state, documents };

  default:
    return state;
  }
}

// Action Creators

export function fetchDocuments() {
  return (dispatch) => {
    dispatch({ type: FETCH_DOCUMENTS_REQUEST });

    request
    .get('/api/documents')
    .end(function (err, res) {
      if (err) {
        dispatch({ type: REQUEST_ERROR, error: err });
        return;
      }

      // Normalize the response and only pull out the document bodies.
      const norm = normalizeDocuments(res.body);

      dispatch({ type: UPDATE_DOCUMENTS, documents: norm.entites.documents });
    });
  };
}
