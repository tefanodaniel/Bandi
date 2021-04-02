import { CHAT_LOGIN } from '../actions/types';

export function chatLogin(data) {
  return (dispatch) => {
    dispatch({
      payload: data,
      type: CHAT_LOGIN
    })
  }
}
