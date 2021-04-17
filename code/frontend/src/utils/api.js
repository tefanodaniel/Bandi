import axios from 'axios';
import config from '../config';
import Cookies from "js-cookie";
import MusicianApi from "./MusicianApiService";
import { CometChat } from "@cometchat-pro/chat";

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

export function chatApiInstance() {
  return axios.create({
    baseURL: "https://api-us.cometchat.io/v2.0",
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
