import { chatApiInstance } from "./api";

class ChatAPIService {
    constructor() {
      this.chatApiInstance = chatApiInstance();
    }

    getCurrentUser(uid) {
      return this.chatApiInstance.get(`/${uid}/me`);
    }

    async accountExists(uid) {
      try {
        let res = await this.getCurrentUser(uid);
        return res.data.data;
      } catch (e) {
        if (e.response.data.error.code === "ERR_UID_NOT_FOUND") {
          return false;
        } else {
          console.log("Encounted other error with chat API");
          return false;
        }
      }
    }

    createUserAuthToken(uid) {
      return this.chatApiInstance.post(`/${uid}/auth_tokens`);
    }

    getUserAuthTokens(uid) {
      return this.chatApiInstance.get(`/${uid}/auth_tokens`);
    }

    async hasAuthTokens(uid) {
      try {
        let res = await this.getUserAuthTokens(uid);
        return res.data.meta.pagination.total;
      } catch (e) {
        console.log("Encountered error while counting user auth tokens", e);
        return null;
      }
    }

    deleteAllUserAuthTokens(uid) {
      return this.chatApiInstance.delete(`/${uid}/auth_tokens`);
    }

    addChatFriend(uid, friendUID) {
      let data = {"accepted": [friendUID]}
      return this.chatApiInstance.post(`/${uid}/friends`, data);
    }
}

export default new ChatAPIService();
