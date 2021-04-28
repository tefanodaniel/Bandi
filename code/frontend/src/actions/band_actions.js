import { LOAD_BANDS_FOR_MUSICIAN } from './types';
import BandApi from "../utils/BandApiService";

export const fetchBandsForMusician = (attr) => (dispatch) => {
  return BandApi.get(attr).then(response => dispatch({
    payload: response.data,
    type: LOAD_BANDS_FOR_MUSICIAN
  })).catch(error => console.log(error))
}
