import { USER_LOGIN, USER_LOGOUT, UPDATE_USER_PROFILE, USER_CHAT_LOGIN, LOAD_USER_BANDS } from './types';
import MusicianApi from "../utils/MusicianApiService";
import BandApi from "../utils/BandApiService";

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

export const fetchUserBands = (id) => (dispatch) => {
  return BandApi.getUserBands(id).then(response => dispatch({
    payload: response.data,
    type: LOAD_USER_BANDS
  }))
}
