import customhttp from "./api";

class SDEventAPIService {
    getAll() {
        return customhttp.get("/events");
    }

    create(data) {
        return customhttp.post("/events", data);
    }
}

export default new SDEventAPIService();
