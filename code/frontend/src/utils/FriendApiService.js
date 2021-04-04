import customhttp from "./api";

class FriendApiService {

    getUserFriendList(id) {
        return customhttp.get(`/friends/${id}`);
    }

    getIncomingFriendRequests(recipientID) {
        return customhttp.get(`/requests/in/${recipientID}`);
    }

    getOutgoingFriendRequests(senderID) {
        return customhttp.get(`/requests/out/${senderID}`);
    }

    sendFriendRequest(senderID, recipientID) {
        return customhttp.post(`/request/${senderID}/${recipientID}`);
    }

    respondToFriendRequest(senderID, recipientID, action) {
        return customhttp.delete(`/request/${senderID}/${recipientID}/${action}`);
    }

} 
export default new FriendApiService();