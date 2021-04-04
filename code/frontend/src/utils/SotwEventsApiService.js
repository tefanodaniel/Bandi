import customhttp from "./api";

class SotwEventsApiService {
    getAll() {
        return customhttp.get("/sotwevents");
    }

    get(eventid) {
        return customhttp.get(`/sotwevents/${eventid}`);
    }

    create(data) {
        return customhttp.post("/sotwevents", data);
    }

    update(eventid, data) {
        return customhttp.put(`/sotwevents/${eventid}`, data);
    }

    readAllSubmissions(eventid) {
        return customhttp.get(`/sotwevents/submissions/${eventid}`);
    }

    addSubmissiontoEvent(eventid, submissionid) {
        return customhttp.put(`sotwevents/submissions/${eventid}/${submissionid}`);
    }

    removeSubmissionFromEvent(eventid, submissionid) {
        return customhttp.put(`sotwevents/submissions/${eventid}/${submissionid}`);
    }

    delete(eventid) {
        return customhttp.delete(`/sotwevents/${eventid}`);
    }
}

export default new SotwEventsApiService();