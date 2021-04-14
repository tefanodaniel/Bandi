import {
    LOAD_SOTW_EVENTS_INITIAL,
    LOAD_SOTW_EVENTS_CURRENT,
    LOAD_SOTW_EVENTS_CURRENT_SONG,
    LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS
} from './types';
import SotwEventsApi from "../utils/SotwEventsApiService";
import SongApi from "../utils/SongApiService";


// thunk function to load list of all musicians.
// in the future this should be a list of contextual "featured" musicians pre-any user defined search.
// also pagination or carousel.
export async function fetchSotwEvents(dispatch, getState) {
    //console.log("Inside fetchSotwEvents action");
    const response = await SotwEventsApi.getAll();
    dispatch({
        type : LOAD_SOTW_EVENTS_INITIAL,
        payload : response.data
    })
}

export function getCurrentEvent(eventid) {
    //console.log("Inside getCurrentEvent");
    return async function fetchSotwEventCurrent(dispatch, getState) {
        //for this iteration I'm hardcoding which event to show.
        //let eventid = "00001fakeeventid";
        //console.log("Inside fetchSotwEventCurrent action");
        const response = await SotwEventsApi.get(eventid);
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT,
            payload : response.data
        })
    }
}

export function getCurrentEventSong(songId) {
    //console.log("Inside getCurrentEventSong");
    if(songId===1) {
        return null
    }
    return async function fetchSotwEventCurrentSong(dispatch, getState) {
        //for this iteration I'm hardcoding which event to show.
        //let eventid = "00001fakeeventid";
        //console.log("Inside fetchSotwEventCurrentSong action");
        const response = await SongApi.get(songId);
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT_SONG,
            payload : response.data
        })
    }
}

export function getCurrentEventSubmissions(eventId) {
    //console.log("Inside getCurrentEventSubmissions");
    return async function fetchSotwEventCurrentSubmissions(dispatch, getState) {
        const response = await SotwEventsApi.readAllSubmissions(eventId);
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS,
            payload : response.data
        })
    }
}

