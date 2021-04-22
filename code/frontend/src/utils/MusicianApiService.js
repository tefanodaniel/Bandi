import customhttp from "./api";

class MusicianAPIService {
    getAll() {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.get("/musicians");
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in getting musicians. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

    get(id) {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.get(`/musicians/${id}`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in getting musician. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

    create(data) {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.post("/musicians", data);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in creating musician. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

    update(id, data) {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.put(`/musicians/${id}`, data);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in updating musician. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

    delete(id) {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.delete(`/musicians/${id}`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in getting deleting musician. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

    deleteAll() {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.delete(`/musicians`);
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in deleting musicians. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }

    findByQuery(queryparams) {
        let tries = 0;
        while (tries < 5) {
            try {
                let response = customhttp.get(`/musicians`, { params: queryparams });
                if (response) {
                    return response;
                }
            } catch (e) {
                console.log("Error in finding musicians. Retrying...", e);
                tries++;
            }
        }
        alert("Fatal error encountered, please refresh the page to continue using Bandi.")
    }
}

export default new MusicianAPIService();