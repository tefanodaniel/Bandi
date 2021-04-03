import { CHAT_LOGIN, CHAT_LOGOUT } from '../actions/types';
import ChatApi from "../utils/ChatApiService";
import { CometChat } from "@cometchat-pro/chat";
import config from '../config';
import Cookies from "js-cookie";
import store from "../store";

export function chatLogin(userId, name) {
  return async function doChatLogin() {
    let res1 = await ChatApi.accountExists(userId)
    if (res1) {
      console.log("Found existing account for this user");
    } else {
      // Create account for this user
      await createCometChatUser(userId, name);
    }
    // Create new auth token for this user
    let res2 = await ChatApi.createUserAuthToken(userId);
    setTimeout(() => {logInCometChatUser(res2.data.data.authToken);}, 1000);
  }
}

export function chatLogout() {
  CometChat.logout().then(() => {
    console.log("Chat logout completed successfully");
    dispatch({
      type: CHAT_LOGOUT
    });
  }, error => {
    //Logout failed with exception
    console.log("Chat logout failed with exception:",{error});
  });
}

///////////////////////// Helper functions //////////////////////////
function createCometChatUser(uid, name) {
  var user = new CometChat.User(uid);
  user.setName(name);

  CometChat.createUser(user, config.apiKey).then(
    user => {
      console.log("User was successfully created: ", user);
    },error => {
      console.log("Could not create user: ", error);
    }
  );
}

function logInCometChatUser(userAuthToken) {
  console.log("USING AUTH: ", userAuthToken);
  CometChat.login(userAuthToken).then(
    user => {
      console.log("Chat login was successful: ", Cookies.get('id'));
      // Dispatch action
      store.dispatch({
        payload: user,
        type: CHAT_LOGIN
      });
    },
    error => {
      console.log("Login failed with exception:", { error });
    }
  );
}
