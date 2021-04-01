import { USER_LOGIN, USER_LOGOUT, UPDATE_USER_PROFILE } from '../actions/types';

export default function userReducer(state = {}, action) {
    switch (action.type) {
        case USER_LOGIN: {
            return action.payload;
        }
        case USER_LOGOUT: {
            return null;
        }
        case UPDATE_USER_PROFILE:
            return action.payload;
        default:
            return state;
    }
}
