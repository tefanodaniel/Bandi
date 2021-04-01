import { LOAD_MUSICIANS_INITIAL, LOAD_MUSICIANS_QUERY, CLEAR_MUSICIANS_QUERY } from './types';
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

export function newQuery(queryparams) {
  console.log("Inside newQuery action");
    return async function fetchMusiciansNameQuery(dispatch, getState) {
        //const queryparams = { name : input }
        const response = await MusicianApi.findByQuery(queryparams)
        //console.log(response);
        dispatch({
            type : LOAD_MUSICIANS_QUERY,
            payload : response.data
        })
    }
}

export const clearQuery = {
    type : CLEAR_MUSICIANS_QUERY,
    payload : []
}
