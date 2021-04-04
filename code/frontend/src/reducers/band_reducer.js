import { LOAD_BANDS_FOR_MUSICIAN } from './types';

export default function bandReducer (state = {}, action) {
    switch (action.type) {
        case LOAD_BANDS_FOR_MUSICIAN:
            return {
                ...state,
                bands: action.payload,
            }
        default:
            return state
    }
}
