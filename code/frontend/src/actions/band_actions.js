import { LOAD_USER_BANDS } from './types';
import BandApi from "../utils/BandApiService";

export const fetchBandsForMusician = (id) => (dispatch) => {
  return BandApi.get(id).then(response => dispatch({
    payload: response.data,
    type: LOAD_USER_BANDS
  }))
}
