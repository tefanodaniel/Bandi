import {
    LOAD_MUSICIANS_INITIAL,
    LOAD_MUSICIANS_QUERY,
    CLEAR_MUSICIANS_QUERY,
    LOAD_MUSICIANS_INITIAL_SEARCH
} from '../actions/types';

export default function musicianReducer (state = {}, action) {
    switch (action.type) {
        case LOAD_MUSICIANS_INITIAL:
            return {
                ...state,
                musicians: action.payload,
                filteredMusicians: action.payload,
                query: null
            }
        case LOAD_MUSICIANS_INITIAL_SEARCH:
            return {
                ...state,
                filteredMusicians: action.payload,
                query: null
            }
        case LOAD_MUSICIANS_QUERY:
            console.log(action.payload)
            return {
                ...state,
                filteredMusicians: action.payload.res_data,
                query: action.payload.query
            }
        case CLEAR_MUSICIANS_QUERY:
            return {
                ...state,
                filteredMusicians: action.payload.res_data,
                query: action.payload.query
            }
        default:
            return state
    }
}
