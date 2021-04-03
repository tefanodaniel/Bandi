import customhttp from "./api";

class BandAPIService {
    getAll() {
        return customhttp.get("/bands");
    }

    get(id) {
        return customhttp.get(`/bands/${id}`);
    }

    create(data) {
        return customhttp.post("/bands", data);
    }

    update(bid, mid) {
        return customhttp.put(`/bands/${bid}/${mid}`);
    }

    deleteBandMember(bid, mid) {
        return customhttp.delete(`/bands/${bid}/${mid}`);
    }

    findByQuery(queryParams) {
        return customhttp.get(`/bands`, { params: queryParams });
    }

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

export default new BandAPIService();
