import {
    LOAD_USER_FRIENDS,
    LOAD_INCOMING_FRIEND_REQUESTS,
    LOAD_OUTGOING_FRIEND_REQUESTS
} from './types';
import RequestApiService from '../utils/RequestApiService';


export function getUserFriends(id) {
    return async function fetchFriends(dispatch, getState) {
        const response = await RequestApiService.getUserFriendList(id);
        return dispatch({
            type : LOAD_USER_FRIENDS,
            payload : response.data
        })
    }
}

export function getIncomingFriendRequests(recipientID) {
    return async function fetchIncomingFriendRequests(dispatch, getState) {
        const response = await RequestApiService.getIncomingFriendRequests(recipientID);
        return dispatch({
            type : LOAD_INCOMING_FRIEND_REQUESTS,
            payload : response.data
        })
    }
}

export function getOutgoingFriendRequests(senderID) {
    return async function fetchOutgoingFriendRequests(dispatch, getState) {
        const response = await RequestApiService.getOutgoingFriendRequests(senderID);
        return dispatch({
            type : LOAD_OUTGOING_FRIEND_REQUESTS,
            payload : response.data
        })
    }
}


