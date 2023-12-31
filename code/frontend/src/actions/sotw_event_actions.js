import {
    LOAD_SOTW_EVENTS_INITIAL,
    LOAD_SOTW_EVENTS_CURRENT,
    LOAD_SOTW_EVENTS_QUERY,
    LOAD_SOTW_EVENTS_CURRENT_SONG,
    LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS,
    CREATE_NEW_USER_SUBMISSION,
    CREATE_NEW_SOTW_EVENT_GIVEN_GENRE,
    UPDATE_CLOCK_STATE,
} from './types';
import SotwEventsApi from "../utils/SotwEventsApiService";
import SotwSubmissionsApi from "../utils/SotwSubmissionsApiService";
import SongApi from "../utils/SongApiService";
import {delay} from "../utils/miscellaneous";

// the action creators below are thunk functions.
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
        //console.log("Inside fetchSotwEventCurrent action");
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
        if(!response1.data){
            alert('No such event, please choose a different genre and(or) week');
            return
        }
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
        //console.log("Inside fetchSotwEventCurrentSong action");
        const response = await SongApi.get(songId);
        dispatch({
            type : LOAD_SOTW_EVENTS_CURRENT_SONG,
            payload : response.data
        })
    }
}

export function getCurrentEventSubmissions(eventId) {
    return async function fetchSotwEventCurrentSubmissions(dispatch, getState) {
        if(eventId === -1){
            return;
        }
        console.log('fetching submissions for current event: ', eventId);
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
        await delay(1000);
        //if(response1.data === null)
        //    console.log('could not create submission')
        //    return
        console.log('going to add submission to event')
        const response2 = await SotwEventsApi.addSubmissiontoEvent(event_id, submission_id);
        //console.log(response);
        alert('Thank you for your submission!')
        dispatch({
            type : CREATE_NEW_USER_SUBMISSION,
            payload : response2.data
        })
        dispatch(getCurrentEventSubmissions(event_id))
    }
}

export function newEventByGenreWrapper(eventId, genre, startDay, endDay) {
    console.log("Inside newEventByGenre action");
    return async function newEventGenre(dispatch, getState) {
        let eventparams = {}
        eventparams.eventId = eventId;
        eventparams.genre = genre;
        eventparams.startDay = startDay;
        eventparams.endDay = endDay;
        eventparams.adminId = "22xpmsx47uendfh4kafp3zjmi";

        const response = await SotwEventsApi.findEvent(eventparams.genre, eventparams.startDay, eventparams.endDay)
        if(response.data !== null){
            return
        }
        await delay(1000);
        //console.log("NO EVENT YET FOR GENRE ", genre);
        const response1 = await SongApi.generate(genre);
        await delay(1000);

        eventparams.songId = response1.data.songId;
        const response2 = await SotwEventsApi.create(eventparams);
        dispatch({
            type : CREATE_NEW_SOTW_EVENT_GIVEN_GENRE,
            payload : []
        })
        dispatch(fetchSotwEvents)
    }
}

export function getSubmissionsWrapper(submissions_ids, eventId) {
    console.log("Inside getSubmissionsWrapper");
    return async function getSubmissions(dispatch, getState) {
        dispatch(getCurrentEventSubmissions(eventId))
    }
}
