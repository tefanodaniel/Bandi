import { LOAD_USER_BANDS } from '../actions/types';

export default function bandReducer (state = {}, action) {
    switch (action.type) {
        case LOAD_USER_BANDS:
            return {
                ...state,
                bands: action.payload,
            }
        default:
            return state
    }
}
