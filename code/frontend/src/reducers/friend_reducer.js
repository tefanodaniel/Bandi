import {
    LOAD_USER_FRIENDS,
    LOAD_INCOMING_FRIEND_REQUESTS,
    LOAD_OUTGOING_FRIEND_REQUESTS
} from '../actions/types';

export default function friendReducer(state = {}, action) {
    switch (action.type) {
        case LOAD_USER_FRIENDS:
            return {
                ...state,
                friend_info: action.payload,
            }
        case LOAD_INCOMING_FRIEND_REQUESTS:
            return {
                ...state,
                incoming_friend_requests: action.payload
            }
        case LOAD_OUTGOING_FRIEND_REQUESTS:
            return {
                ...state,
                outgoing_friend_requests: action.payload
            }

        default:
            return state
    }
}