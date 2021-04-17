import React from 'react';
import ReactDOM from 'react-dom';
import {Provider, useDispatch} from 'react-redux';
import './styles/index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import Cookies from "js-cookie";
import { CometChat } from "@cometchat-pro/chat"
import config from './config';
import {CreateSotwEvents} from "./utils/miscellaneous";
//import { useDispatch } from "react-redux";


import store from "./store";
import { fetchMusicians } from "./actions/musician_actions";

import {fetchSDEvents} from "./actions/sd_event_actions";
import {fetchSotwEvents, newEventByGenreWrapper} from "./actions/sotw_event_actions";
import {createSotwEvents} from "./utils/miscellaneous";

import {CronJob} from 'cron';
import {endOfWeek, format, startOfWeek} from "date-fns";
import {shazam_genre_api_names} from "./utils/miscellaneous";
// Redux store
console.log('Initial state: ', store.getState())
const unsubscribe = store.subscribe(() =>
    console.log('')
//    console.log('State after dispatch: ', store.getState())
)
store.dispatch(fetchMusicians)

store.dispatch(fetchSDEvents)

store.dispatch(fetchSotwEvents)

console.log('Before job instantiation');
const job = new CronJob('0 */10 6 * * 0', function() {
    const d = new Date();
    console.log('At 06:00:00 on a Sunday:', d);
    console.log('Creating New SOTW Events')

    let startDay = format(startOfWeek(new Date()), 'PPP');
    startDay = startDay.replace(/st|nd|rd|th/,'');

    let endDay = format(endOfWeek(new Date()), 'PPP');
    endDay = endDay.replace(/st|nd|rd|th/,'');

    const genres = shazam_genre_api_names();
    const uuid = require("uuid");

    for(let i=0; i < 13; i++) {
        let eventId = uuid.v4();
        store.dispatch(newEventByGenreWrapper(eventId, genres[i], startDay, endDay));
    }
});
console.log('After job instantiation');
job.start();

// Init CometChat
var appID = config.appId;
var region = config.region;
var appSetting = new CometChat.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();
CometChat.init(appID, appSetting).then(
  () => {
    console.log("CometChat initialization completed successfully");
    //Cookies.set('chatInitialized', true);
    store.dispatch({
      type: 'chat/initialize'
    });
  },
  error => {
    console.log("CometChat initialization failed with error:", error);
    //Cookies.set('chatInitialized', false);
    // Check the reason for error and take appropriate action.
  }
);

ReactDOM.render(
  <React.StrictMode>
      <Provider store={store}>
          <App />
      </Provider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
