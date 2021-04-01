import { chatApiInstance } from "./api";

class ChatAPIService {
    constructor() {
      this.chatApiInstance = chatApiInstance();
    }

    getCurrentUser(uid) {
        return this.chatApiInstance.get(`/${uid}/me`);
    }

    // get(id) {
    //     return customhttp.get(`/musicians/${id}`);
    // }
    //
    // create(data) {
    //     return customhttp.post("/musicians", data);
    // }
    //
    // update(id, data) {
    //     return customhttp.put(`/musicians/${id}`, data);
    // }
    //
    // delete(id) {
    //     return customhttp.delete(`/musicians/${id}`);
    // }
    //
    // deleteAll() {
    //     return customhttp.delete(`/musicians`);
    // }
    //
    // findByQuery(queryparams) {
    //     return customhttp.get(`/musicians`, { params: queryparams });
    // }
}

export default new ChatAPIService();
