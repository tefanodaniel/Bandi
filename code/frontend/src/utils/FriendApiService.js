import customhttp from "./api";

class FriendApiService {


    getUserFriendList(id) {
        return customhttp.get(`/friends/${id}`);
    }

    getIncomingFriendRequests(recipientID) {
        let type = "friend";
        return customhttp.get(`/requests/${type}/in/${recipientID}`);
    }
    
    getOutgoingFriendRequests(senderID) {
        let type = "friend";
        return customhttp.get(`/requests/${type}/out/${senderID}`);
    }
    
    sendFriendRequest(senderID, recipientID) {
        let type = "friend";
        return customhttp.post(`/request/${type}/${senderID}/${recipientID}`);
    }
    
    respondToFriendRequest(senderID, recipientID, action) {
        let type = "friend";
        return customhttp.delete(`/request/${type}/${senderID}/${recipientID}/${action}`);
    }

} 
export default new FriendApiService();