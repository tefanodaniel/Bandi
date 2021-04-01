import customhttp from "./api";

class SDEventAPIService {
    getAll() {
        return customhttp.get("/speeddateevents");
    }

    get(id) {
        return customhttp.get(`/speeddateevents/${id}`);
    }

    create(data) {
        return customhttp.post("/speeddateevents", data);
    }

    addParticipant(eid, mid) {
        return customhttp.put(`/speeddateevents/${eid}/${mid}`);
    }

    deleteParticipant(eid, mid) {
        return customhttp.delete(`/speeddateevents/${eid}/${mid}`);
    }
}

export default new SDEventAPIService();
