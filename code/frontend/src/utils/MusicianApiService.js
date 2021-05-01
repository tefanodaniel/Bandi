import customhttp from "./api";
import querystring from 'query-string';

class MusicianAPIService {
    getAll() {
        return customhttp.get("/musicians");
    }

    get(id) {
        return customhttp.get(`/musicians/${id}`);
    }

    create(data) {
        return customhttp.post("/musicians", data);
    }

    update(id, data) {
        return customhttp.put(`/musicians/${id}`, data);
    }

    delete(id) {
        return customhttp.delete(`/musicians/${id}`);
    }

    deleteAll() {
        return customhttp.delete(`/musicians`);
    }

    findByQuery(queryparams) {
        console.log('what are the params:,', queryparams)
        return customhttp.get("/musicians", { 
            params: queryparams, 
            paramsSerializer: params => {
                return querystring.stringify(params)
            } 
        });
    }

    updateShowTopTracks(id, data) {
        return customhttp.put(`/showtoptracks/${id}`, data);
    }
}

export default new MusicianAPIService();