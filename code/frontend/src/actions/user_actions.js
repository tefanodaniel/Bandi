import { USER_LOGIN, USER_LOGOUT } from './types';
//import { logout } from "../utils/api";
import MusicianAPI from "../utils/MusicianApiService";

//thunk function to do user-login.
export function getUser(id) {
    return async function fetchUserData(dispatch, getState) {
        const response = await MusicianAPI.get(id)
        console.log('the response is ', response);
        dispatch({
            type: USER_LOGIN,
            payload : response.data
        })
    }
}
