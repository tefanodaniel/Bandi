import {
    LOAD_SOTW_EVENTS_INITIAL,
    LOAD_SOTW_EVENTS_CURRENT,
    LOAD_SOTW_EVENTS_QUERY,
    LOAD_SOTW_EVENTS_CURRENT_SONG,
    LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS,
    CREATE_NEW_USER_SUBMISSION,
    UPDATE_CLOCK_STATE,
    ADD_SUBMISSION_TO_EVENT
} from './types';
import SotwEventsApi from "../utils/SotwEventsApiService";
import SotwSubmissionsApi from "../utils/SotwSubmissionsApiService";
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
        const response = await SotwEventsApi.get(eventid);
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT,
            payload : response.data
        })
    }
}

export function findSotwEventQueryWrapper(eventparams) {
    //console.log("Inside findEventQueryWrapper");
    return async function fetchSotwEventQuery(dispatch, getState) {
        //console.log("Inside fetchSotwEventQuery action");
        //console.log("printing event params")
        //console.log(eventparams.genre)
        //console.log(eventparams.startday)
        //console.log(eventparams.endday)
        const response1 = await SotwEventsApi.findEvent(eventparams.genre, eventparams.startday, eventparams.endday)
        const songId = response1.data.songId
        const response2 = await SongApi.get(songId);
        dispatch({
            type: LOAD_SOTW_EVENTS_QUERY,
            payload : response1.data
        })

        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT_SONG,
            payload : response2.data
        })

    }
}

export function updateClockStateWrapper(temp_dict) {
    //console.log("Inside update clock state action");
    return function updateClockState(dispatch, getState) {
        dispatch({
            type: UPDATE_CLOCK_STATE,
            payload: temp_dict
        })
    }
}


export function getCurrentEventSong(songId) {
    //console.log("Inside getCurrentEventSong");
    if(songId===-1) {
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
    ///console.log("Inside getCurrentEventSubmissions");
    return async function fetchSotwEventCurrentSubmissions(dispatch, getState) {
        const response = await SotwEventsApi.readAllSubmissions(eventId);
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS,
            payload : response.data
        })
    }
}

export function newUserSubmission(event_id, submission_data) {
    console.log("Inside newUserSubmission action");
    return async function createUserSubmission(dispatch, getState) {
        const submission_id = submission_data.submission_id;
        const response1 = await SotwSubmissionsApi.create(submission_data)
        const response2 = await SotwEventsApi.addSubmissiontoEvent(event_id, submission_id);
        //console.log(response);
        dispatch({
            type : CREATE_NEW_USER_SUBMISSION,
            payload : response2.data
        })
    }
}

/*
export function addSubmissionToEvent(info) {
    return async function linkUserSubmissionToEvent(dispatch, getState) {
        const response = await SotwEventsApi.addSubmissiontoEvent(info.event_id, info.submission_id);
        dispatch({
          type : ADD_SUBMISSION_TO_EVENT,
          payload : response.data
        })
    }
}
*/
