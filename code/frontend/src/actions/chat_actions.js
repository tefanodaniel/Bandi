import { CHAT_LOGIN } from '../actions/types';

export function chatLogin(data) {
  console.log("INSIDE CHATLOGIN ACTION");
  return (dispatch) => {
    dispatch({
      payload: data,
      type: CHAT_LOGIN
    })
  }
}
