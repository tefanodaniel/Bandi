import {
    LOAD_USER_FRIENDS,
    LOAD_INCOMING_FRIEND_REQUESTS,
    LOAD_OUTGOING_FRIEND_REQUESTS
} from './types';
import FriendApiService from '../utils/FriendApiService';


export function getUserFriends(id) {
    console.log("Getting user friends");
    return async function fetchFriends(dispatch, getState) {
        const response = await FriendApiService.getUserFriendList(id);
        return dispatch({
            type : LOAD_USER_FRIENDS,
            payload : response.data
        })
    }

}

export async function fetchIncomingFriendRequests(dispatch, getState) {
    const recipientID = getState().id;
    const response = await FriendApiService.getIncomingFriendRequests(recipientID);
    dispatch({
        type : LOAD_INCOMING_FRIEND_REQUESTS,
        payload : response.data
    })
}


export async function fetchOutgoingFriendRequests(dispatch, getState) {
    const senderID = getState().id;
    const response = await FriendApiService.getOutgoingFriendRequests(senderID);
    dispatch({
        type : LOAD_OUTGOING_FRIEND_REQUESTS,
        payload : response.data
    })
}

