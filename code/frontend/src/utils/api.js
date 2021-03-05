import axios from 'axios';

export function loginWithSpotify() {
  //Test API call:
  //return axios.get('https://dog.ceo/api/breeds/list/all');
  return axios.get('http://localhost:4567');
}
