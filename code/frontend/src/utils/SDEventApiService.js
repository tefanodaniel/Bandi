import customhttp from "./api";

class SDEventAPIService {
    getAll() {
        return customhttp.get("/events");
    }
}

export default new SDEventAPIService();
