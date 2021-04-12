import {
    LOAD_MUSICIANS_INITIAL,
    LOAD_MUSICIANS_QUERY,
    CLEAR_MUSICIANS_QUERY,
    LOAD_MUSICIANS_INITIAL_SEARCH
} from './types';
import MusicianApi from "../utils/MusicianApiService";


// thunk function to load list of all musicians.
// in the future this should be a list of contextual "featured" musicians pre-any user defined search.
// also pagination or carousel.
export async function fetchMusicians(dispatch, getState) {
    console.log("Inside fetchMusicians action");
    const response = await MusicianApi.getAll();
    dispatch({
        type : LOAD_MUSICIANS_INITIAL,
        payload : response.data
    })
}

export function allMusiciansQuery(queryparams) {
    console.log("Inside all Musicians query");
    return async function fetchAllMusicians(dispatch, getState) {
        const response = await MusicianApi.getAll(); // the backend display of all musicians except logged in user needs to be implemented.
        dispatch({
            type : LOAD_MUSICIANS_INITIAL_SEARCH,
            payload : response.data
        })
    }
}

export function newQuery(queryparams) {
  console.log("Inside newQuery action");
    return async function fetchMusiciansNameQuery(dispatch, getState) {
        //const queryparams = { name : input }
        const response = await MusicianApi.findByQuery(queryparams)
        //console.log(response);
        dispatch({
            type : LOAD_MUSICIANS_QUERY,
            payload : {res_data : response.data, query: queryparams}
        })
    }
}

export const clearQuery = {
    type : CLEAR_MUSICIANS_QUERY,
    payload : {res_data: null, query: null}
}
