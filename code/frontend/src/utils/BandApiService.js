import customhttp from "./api";

class BandAPIService {
    getAll() {
        return customhttp.get("/bands");
    }

    get(attr) {
        return customhttp.get(`/band/${attr.id}`);
    }

    create(data) {
        return customhttp.post("/bands", data);
    }

    update(bid, mid, data) {
        return customhttp.put(`/bands/${bid}/${mid}`, data);
    }

    deleteBandMember(bid, mid) {
        return customhttp.delete(`/bands/${id}`);
    }

    findByQuery(queryParams) {
        return customhttp.get(`/bands`, { params: queryParams });
    }
}

export default new BandAPIService();
