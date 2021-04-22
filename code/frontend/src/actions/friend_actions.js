import {
    LOAD_USER_FRIENDS,
    LOAD_INCOMING_FRIEND_REQUESTS,
    LOAD_OUTGOING_FRIEND_REQUESTS,
    TAKE_ACTION_ON_FRIEND_REQUEST,
    SEND_FRIEND_REQUEST
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

export function sendFriendRequest(senderID, recipientID) {
    return async function dispatchFriendRequest(dispatch, getState) {
        const response = await RequestApiService.sendFriendRequest(senderID,recipientID);
        dispatch(getOutgoingFriendRequests(recipientID));
        return dispatch({
            type: SEND_FRIEND_REQUEST
        })
    }
}


export function takeActionOnFriendRequest(senderID, recipientID, action) {
    return async function dispatchActionOnFriendRequest(dispatch, getState) {
        const response = await RequestApiService.respondToFriendRequest(senderID,recipientID, action);
        dispatch(getIncomingFriendRequests(recipientID));
        dispatch(getUserFriends(recipientID));
        return dispatch({
            type: TAKE_ACTION_ON_FRIEND_REQUEST
        })
    }
}


