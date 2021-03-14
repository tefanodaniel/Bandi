import axios from 'axios';

export function loginWithSpotify() {
  //Test API call:
  //return axios.get('https://dog.ceo/api/breeds/list/all');
  return axios.get('http://localhost:4567');
}

export function findCookie(cookieTitle) {
  var cookies = document.cookie.split(";");
  for (var j = 0; j < cookies.length; j++) {
    var curCookie = cookies[j].split("=");
    var cookieKey = curCookie[0].trim();
    if (cookieKey == cookieTitle) {
      return decodeURIComponent(curCookie[1]);
    }
  }
  return null;
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
  document.cookie = "id=;expires=0";
}