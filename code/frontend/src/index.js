import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import './styles/index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import store from "./store";
import { fetchMusicians } from "./actions/musician_actions";

console.log('Initial state: ', store.getState())

const unsubscribe = store.subscribe(() =>
    console.log('State after dispatch: ', store.getState())
)

store.dispatch(fetchMusicians)


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
