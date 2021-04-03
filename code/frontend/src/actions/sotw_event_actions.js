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
    console.log("Inside fetchSotwEvents action");
    const response = await SotwEventsApi.getAll();
    dispatch({
        type : LOAD_SOTW_EVENTS_INITIAL,
        payload : response.data
    })
}

export function getCurrentEvent(eventid) {
    console.log("Inside getCurrentEvent");
    return async function fetchSotwEventCurrent(dispatch, getState) {
        //for this iteration I'm hardcoding which event to show.
        //let eventid = "00001fakeeventid";
        console.log("Inside fetchSotwEventCurrent action");
        const response1 = await SotwEventsApi.get(eventid);
        let songid = response1.data.songid;
        console.log(response1.data);
        console.log("extracted song id is ", songid);
        const response2 = await SongApi.get(songid);
        console.log(response2.data);
        const response3 = await SotwEventsApi.readAllSubmissions(eventid);
        console.log(response3.data);
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT,
            payload : response1.data
        })
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT_SONG,
            payload : response2.data
        })
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS,
            payload : response3.data
        })
    }
}

