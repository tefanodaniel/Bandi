import customhttp from "./api";

class FriendApiService {

    getUserFriendList(id) {
        return customhttp.get(`/friends/${id}`);
    }

    getIncomingFriendRequests(recipientID) {
        return customhttp.get(`/requests/friend/in/${recipientID}`);
    }

    getOutgoingFriendRequests(senderID) {
        return customhttp.get(`/requests/friend/out/${senderID}`);
    }

    sendFriendRequest(senderID, recipientID) {
        return customhttp.post(`/request/friend/${senderID}/${recipientID}`);
    }

    respondToFriendRequest(senderID, recipientID, action) {
        return customhttp.delete(`/request/friend/${senderID}/${recipientID}/${action}`);
    }

} 
export default new FriendApiService();