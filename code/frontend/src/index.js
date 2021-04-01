import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import './styles/index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import Cookies from "js-cookie";
import { CometChat } from "@cometchat-pro/chat"
import config from './config';

import store from "./store";
import { fetchMusicians } from "./actions/musician_actions";

import {fetchSDEvents} from "./actions/sd_event_actions";

// Redux store
console.log('Initial state: ', store.getState())
const unsubscribe = store.subscribe(() =>
    console.log('State after dispatch: ', store.getState())
)
store.dispatch(fetchMusicians)

store.dispatch(fetchSDEvents)

// Init CometChat
var appID = config.appId;
var region = config.region;
var appSetting = new CometChat.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();
CometChat.init(appID, appSetting).then(
  () => {
    console.log("CometChat initialization completed successfully");
    //Cookies.set('chatInitialized', true);
    // You can now call login function.
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
