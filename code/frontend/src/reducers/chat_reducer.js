import { CHAT_INITIALIZE, CHAT_LOGIN, CHAT_LOGOUT } from '../actions/types';

export default function chatReducer(state = {loggedIn: false, initialized: false}, action) {
    switch (action.type) {
        case CHAT_INITIALIZE:
          return { ...state, initialized: true }
        case CHAT_LOGIN:
          return { ...state, loggedIn: true, ...action.payload };
        case CHAT_LOGOUT:
            return { initialized: true, loggedIn: false };
        default:
            return state;
    }
}
