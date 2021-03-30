import { LOAD_SD_EVENTS } from './types';
import SDEventApi from "../utils/SDEventApiService";

export const fetchSDEvents = (attr) => (dispatch) => {
    return SDEventApi.get(attr).then(response => dispatch({
        payload: response.data,
        type: LOAD_SD_EVENTS
    }))
}