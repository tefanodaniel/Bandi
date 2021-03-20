import MusicianApi from "../../utils/MusicianApiService";

const initialState = []

// thunk function to load list of all musicians.
// in the future this should be a list of contextual "featured" musicians pre-any user defined search.
// also pagination or carousel.

export async function fetchMusicians(dispatch, getState) {
    const response = await MusicianApi.getAll();
    dispatch({
        type : 'musicians/loadfirst',
        payload : response.data
    })
}

export function newQuery(queryparams) {
    return async function fetchMusiciansNameQuery(dispatch, getState) {
        //const queryparams = { name : input }
        const response = await MusicianApi.findByQuery(queryparams)
        console.log(response);
        dispatch({
            type : 'musicians/newquery',
            payload : response.data
        })
    }
}

export const clearQuery = {
    type : 'musicians/clearquery',
    payload : []
}

export default function MusicianReducer (state = initialState, action) {
    switch (action.type) {
        case 'musicians/loadfirst':
            return {
                ...state,
                musicians: action.payload,
                filteredMusicians: action.payload
            }
        case 'musicians/newquery':
            return {
                ...state,
                filteredMusicians: action.payload
            }
        case 'musicians/clearquery':
            return {
                ...state,
                filteredMusicians: action.payload
            }
        default:
            return state
    }
}