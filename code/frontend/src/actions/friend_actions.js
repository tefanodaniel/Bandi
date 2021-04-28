import {
    LOAD_USER_FRIENDS,
    LOAD_INCOMING_FRIEND_REQUESTS,
    LOAD_OUTGOING_FRIEND_REQUESTS
} from './types';
import FriendApiService from '../utils/FriendApiService';


export function getUserFriends(id) {
    return async function fetchFriends(dispatch, getState) {
        const response = await FriendApiService.getUserFriendList(id);
        return dispatch({
            type : LOAD_USER_FRIENDS,
            payload : response.data
        })
    }
}

export function getIncomingFriendRequests(recipientID) {
    return async function fetchIncomingFriendRequests(dispatch, getState) {
        const response = await FriendApiService.getIncomingFriendRequests(recipientID);
        return dispatch({
            type : LOAD_INCOMING_FRIEND_REQUESTS,
            payload : response.data
        })
    }
}

export function getOutgoingFriendRequests(senderID) {
    return async function fetchOutgoingFriendRequests(dispatch, getState) {
        const response = await FriendApiService.getOutgoingFriendRequests(senderID);
        return dispatch({
            type : LOAD_OUTGOING_FRIEND_REQUESTS,
            payload : response.data
        })
    }
}
