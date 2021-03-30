import {LOAD_SD_EVENTS} from "./types";

export default function sdEventReducer (state = {}, action) {
    switch (action.type) {
        case LOAD_SD_EVENTS:
            return {
                ...state,
                sdEvents: action.payload,
            }
        default:
            return state
    }
}