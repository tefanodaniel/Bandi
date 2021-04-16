import {shazamChartApiInstance} from "./api";

class ShazamChartApiService {
    constructor() {
        this.shazamChartApiInstance = shazamChartApiInstance();
    }

    getGenre(genre) {
        return this.shazamChartApiInstance.get(`/${genre}`);
    }
}

export default new ShazamChartApiService();