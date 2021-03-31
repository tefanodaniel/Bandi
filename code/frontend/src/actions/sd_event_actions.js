import {LOAD_SD_EVENTS} from './types';
import SDEventApi from "../utils/SDEventApiService";

export async function fetchSDEvents(dispatch, getState) {
    console.log("Inside fetchSDEvents action");
    const response = await SDEventApi.getAll();
    dispatch({
        type : LOAD_SD_EVENTS,
        payload : response.data
    })
}