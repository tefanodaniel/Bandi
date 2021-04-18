import customhttp from "./api";

class SongAPIService {
    getAll() {
        return customhttp.get(`/songs`);
    }

    get(songid) {
        return customhttp.get(`/songs/${songid}`);
    }

    generate(genre) {
        return customhttp.post(`/songs/generate/${genre}`);
    }

    create(data) {
        return customhttp.post("/songs", data);
    }

    update(songid, data) {
        return customhttp.put(`/songs/${songid}`, data);
    }

    delete(songid) {
        return customhttp.delete(`/songs/${songid}`);
    }
}

export default new SongAPIService();
