import { USER_LOGIN, USER_LOGOUT, UPDATE_USER_PROFILE, USER_CHAT_LOGIN } from './types';
import MusicianApi from "../utils/MusicianApiService";

//thunk function to do user-login.
export function getUser(id) {
    return async function fetchUserData(dispatch, getState) {
        const response = await MusicianApi.get(id)
        //console.log('the response is ', response);
        dispatch({
            type: USER_LOGIN,
            payload : response.data
        })
    }
}

export const updateUserProfile = (attr) => (dispatch) => {
  return MusicianApi.update(attr.id, attr).then(response => dispatch({
    payload: response.data,
    type: UPDATE_USER_PROFILE
  }))
}
