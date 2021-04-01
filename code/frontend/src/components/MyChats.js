import React from 'react';
import Cookies from "js-cookie";

import { CometChat } from "@cometchat-pro/chat"
import config from '../config';
import ChatApi from "../utils/ChatApiService";
import { connect } from 'react-redux';

class MyChats extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      userId: Cookies.get('id'),
      chatInitialized: Cookies.get('chatInitialized'),
      loggedIntoChat: Cookies.get('loggedIntoChat'),
      chatUserData: null
    }
  }

  componentDidMount() {
    //console.log(CometChat.getLoggedInUser());
    if (this.state.chatInitialized && this.state.loggedIntoChat) {
      ChatApi.getCurrentUser(this.state.userId).then((res) => {
          this.setState({chatUserData: res.data.data});
        });
    }

  }

  render() {
    return (
      <div>
        <p>{Cookies.get('chatInitialized')}</p>
        <p>{Cookies.get('loggedIntoChat')}</p>
      </div>
      );
  }
}

//export default MyChats;

function mapStateToProps(state) {
  return {
    store: state
  };
} // end mapStateToProps
export default connect(mapStateToProps, null)(MyChats);
