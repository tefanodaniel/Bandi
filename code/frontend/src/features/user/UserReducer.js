import { logout } from "../../utils/api";
import MusicianAPI from "../../utils/MusicianApiService";

const initialState = []

//thunk function to do user-login.
export function getUser(id) {
    return async function fetchUserData(dispatch, getState) {
        const response = await MusicianAPI.get(id)
        console.log('the response is ', response);
        dispatch({
            type: 'user/login',
            payload : response.data
        })
    }
}



export default function UserReducer (state = initialState, action) {
    switch (action.type) {
        case 'user/login': {
            return action.payload
        }
        case 'user/logout': {
            return null
        }
        default:
            return state
    }
}