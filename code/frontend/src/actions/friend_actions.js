import {
    GET_USER_FRIENDS,
    GET_INCOMING_FRIEND_REQUESTS,
    GET_OUTGOING_FRIEND_REQUESTS
} from './types';
import FriendApiService from '../utils/FriendApiService';


export async function getUserFriends(id) {
    const response = await FriendApiService.getUserFriendList();
    dispatch({
        type : GET_USER_FRIENDS,
        payload : response.data
    })
}

export async function getIncomingFriendRequests(recipientID) {
    const response = await FriendApiService.getIncomingFriendRequests(recipientID);
    dispatch({
        type : GET_INCOMING_FRIEND_REQUESTS,
        payload : response.data
    })
}


export async function getOutgoingFriendRequests(senderID) {
    const response = await FriendApiService.getOutgoingFriendRequests(senderID);
    dispatch({
        type : GET_OUTGOING_FRIEND_REQUESTS,
        payload : response.data
    })
}

