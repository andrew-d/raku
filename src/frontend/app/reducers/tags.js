import { UPDATE_TAGS } from 'constants';


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
