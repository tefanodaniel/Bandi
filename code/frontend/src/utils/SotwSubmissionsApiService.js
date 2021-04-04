import customhttp from "./api";

class SotwSubmissionsAPIService {
    getAll() {
        return customhttp.get(`/submissions`);
    }

    get(submissionid) {
        return customhttp.get(`/submissions/${submissionid}`);
    }

    create(data) {
        return customhttp.post("/submissions", data);
    }

    update(submissionid, data) {
        return customhttp.put(`/submissions/${submissionid}`, data);
    }

    delete(submissionid) {
        return customhttp.delete(`/submissions/${submissionid}`);
    }
}

export default new SotwSubmissionsAPIService();
