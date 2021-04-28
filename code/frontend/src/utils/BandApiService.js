import customhttp from "./api";

class BandAPIService {
    getAll() {
        return customhttp.get("/bands");
    }

    getUserBands(userId) {
      return customhttp.get(`/bands?musicianId=${userId}`)
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

}

export default new BandAPIService();
