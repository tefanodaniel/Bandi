import customhttp from "./api";

class RequestApiService {


    getUserFriendList(id) {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.get(`/friends/${id}`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in getting incoming friend requests. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

    getIncomingFriendRequests(recipientID) {
        let type = "friend";
        let tries = 0;
        while (tries < 5) {
            try {
                var response = customhttp.get(`/requests/${type}/in/${recipientID}`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in getting incoming friend requests. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }
    
    getOutgoingFriendRequests(senderID) {
        let type = "friend";
        let tries = 0;
        while (tries < 5) {
            try {
                var response = customhttp.get(`/requests/${type}/out/${senderID}`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in getting outgoing friend requests. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }
    
    sendFriendRequest(senderID, recipientID) {
        let type = "friend";
        let tries = 0;
        while (tries < 5) {
            try {
                var response = customhttp.post(`/request/${type}/${senderID}/${recipientID}`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in sending friend request. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }
    
    respondToFriendRequest(senderID, recipientID, action) {
        let type = "friend";
        let tries = 0;
        while (tries < 5) {
            try {
                var response = customhttp.delete(`/request/${type}/${senderID}/${recipientID}/${action}`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in responding to friend request. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

} 
export default new RequestApiService();