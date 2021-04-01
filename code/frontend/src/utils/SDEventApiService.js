import customhttp from "./api";

class SDEventAPIService {
    getAll() {
        return customhttp.get("/events");
    }

    get(id) {
        return customhttp.get(`/events/${id}`);
    }

    create(data) {
        return customhttp.post("/events", data);
    }
}

export default new SDEventAPIService();
