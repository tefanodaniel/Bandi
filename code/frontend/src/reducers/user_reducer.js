import { USER_LOGIN, USER_LOGOUT } from '../actions/types';

export default function userReducer(state = {}, action) {
    switch (action.type) {
        case USER_LOGIN: {
            return action.payload;
        }
        case USER_LOGOUT: {
            return null;
        }
        default:
            return state;
    }
}
