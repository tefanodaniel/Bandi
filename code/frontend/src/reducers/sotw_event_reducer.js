import {
    LOAD_SOTW_EVENTS_INITIAL,
    LOAD_SOTW_EVENTS_CURRENT,
    LOAD_SOTW_EVENTS_CURRENT_SONG,
    LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS
} from '../actions/types';

export default function sotwEventReducer (state = {}, action) {
    switch (action.type) {
        case LOAD_SOTW_EVENTS_INITIAL:
            return {
                ...state,
                sotw_events: action.payload,
                filtered_events: null,
                chosen_event: null,
                chosen_event_song: null,
                chosen_event_submissions: null
            }
        case LOAD_SOTW_EVENTS_CURRENT:
            return {
                ...state,
                chosen_event: action.payload
            }
        case LOAD_SOTW_EVENTS_CURRENT_SONG:
            return {
                ...state,
                chosen_event_song: action.payload
            }
        case LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS:
            return {
                ...state,
                chosen_event_submissions: action.payload
            }
        default:
            return state
    }
}
