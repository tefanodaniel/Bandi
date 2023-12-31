import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import './styles/index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { CometChat } from "@cometchat-pro/chat"
import config from './config';
import store from "./store";
import { fetchMusicians } from "./actions/musician_actions";
import {fetchSDEvents} from "./actions/sd_event_actions";
import {fetchSotwEvents, newEventByGenreWrapper} from "./actions/sotw_event_actions";
import { CHAT_INITIALIZE } from './actions/types';
import {CronJob} from 'cron';
import {endOfWeek, format, startOfWeek} from "date-fns";
import {shazam_genre_api_names} from "./utils/miscellaneous";
import { v4 as uuidv4 } from 'uuid';
// Redux store
console.log('Initial state: ', store.getState())
const unsubscribe = store.subscribe(() =>
    console.log('Subscriber: State has possibly changed after a dispatched action, ', store.getState())
)

// Initial list of dispatches
console.log("Fetching initial list of musicians")
store.dispatch(fetchMusicians)
console.log("Fetching initial list of speed dating events")
store.dispatch(fetchSDEvents)
console.log("Fetching initial list of song of the week events")
store.dispatch(fetchSotwEvents)

// Start CronJob
console.log('Before job instantiation');
const job = new CronJob('* */2 */2 * * 0', function() {
    const d = new Date();
    console.log('At 06:00:00 on a Sunday:', d);
    console.log('Creating New SOTW Events')

    let startDay = format(startOfWeek(new Date()), 'PPP');
    startDay = startDay.replace(/st|nd|rd|th/,'');

    let endDay = format(endOfWeek(new Date()), 'PPP');
    endDay = endDay.replace(/st|nd|rd|th/,'');

    const genres = shazam_genre_api_names();

    var start1 = 'April 11, 2021';
    var start1 = 'April 18, 2021';
    var start1 = 'April 25, 2021';
    var end1 = 'April 17, 2021';
    var end2 = 'April 24, 2021';
    var end3 = 'May 1, 2021';

    for(let i=0; i < 13; i++) {
        let eventId = uuidv4();
        store.dispatch(newEventByGenreWrapper(eventId, genres[i], startDay, endDay));
        //store.dispatch(newEventByGenreWrapper(eventId, genres[i], start1, end1));
        //store.dispatch(newEventByGenreWrapper(eventId, genres[i], start2, end2));
        //store.dispatch(newEventByGenreWrapper(eventId, genres[i], start3, end3));

    }
});
console.log('After job instantiation');
job.start();

// Init CometChat
var appID = config.appId;
var region = config.region;
var appSetting = new CometChat.AppSettingsBuilder().subscribePresenceForFriends().setRegion(region).build();
CometChat.init(appID, appSetting).then(
  () => {
    console.log("CometChat initialization completed successfully");
    //Cookies.set('chatInitialized', true);
    store.dispatch({
      type: CHAT_INITIALIZE
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
