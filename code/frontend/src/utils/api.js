import axios from 'axios';
import config from '../config';
import Cookies from "js-cookie";
import MusicianApi from "./MusicianApiService";
import { CometChat } from "@cometchat-pro/chat";

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

export async function getFriendsDataFromApi(id) {
    let backend_url = getBackendURL();
    let type = "friend";
    setTimeout(() => {console.log("Delaying 1...");}, 1000)
    let r1 = await axios.get(backend_url + `/friends/${id}`);
    setTimeout(() => {console.log("Delaying 2...");}, 1000)
    let r2 = await axios.get(backend_url + `/requests/${type}/in/${id}`)
    setTimeout(() => {console.log("Delaying 3...");}, 1000)
    let r3 = await axios.get(backend_url + `/requests/${type}/out/${id}`)

    return {'friends': r1.data, 'incoming': r2.data, 'outgoing': r3.data }


}


export function chatApiInstance() {
  return axios.create({
    baseURL: "https://api-us.cometchat.io/v2.0/users",
    headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
        "appId": config.appId,
        "apiKey": config.apiKey
    }
  })
}

export default axios.create({
  baseURL: getBackendURL(),
  headers: {
    "Content-type": "application/json"
  }
});
