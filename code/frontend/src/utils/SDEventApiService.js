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

    addParticipant(eid, mid) {
        return customhttp.put(`/events/${eid}/${mid}`);
    }

    deleteParticipant(eid, mid) {
        return customhttp.delete(`/events/${eid}/${mid}`);
    }
}

export default new SDEventAPIService();
