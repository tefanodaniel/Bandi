import axios from 'axios';
import Cookies from "js-cookie";

export function loginWithSpotify() {
  //Test API call:
  //return axios.get('https://dog.ceo/api/breeds/list/all');
  return axios.get('http://localhost:4567');
}

export function getFrontendURL() {
  const isDev = !process.env.NODE_ENV || process.env.NODE_ENV === "development";
  const url =
      isDev ? "http://localhost:3000"
          : "https://bandiscover.herokuapp.com";
  return url;
}

export function getBackendURL() {
  const isDev = !process.env.NODE_ENV || process.env.NODE_ENV === "development";
  const url =
      isDev ? "http://localhost:4567"
          : "https://bandiscover-api.herokuapp.com";
  return url;
}

export function logout() {
  Cookies.remove('id');
}

export default axios.create({
  baseURL: getBackendURL(),
  headers: {
    "Content-type": "application/json"
  }
});
