import React from 'react';
import Cookies from "js-cookie";

import { CometChat } from "@cometchat-pro/chat"
import { CometChatUI } from "../cometchat-pro-react-ui-kit/CometChatWorkspace/src";
import config from '../config';
import ChatApi from "../utils/ChatApiService";
import { connect } from 'react-redux';

class MyChats extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      userId: Cookies.get('id'),
    }
  }

  componentDidMount() {
    CometChat.getLoggedInUser().then((res) => {
      console.log(res);
    })
  }

  render() {
    return (
      <div style={{width: '800px', height:'800px' }}>
          <CometChatUI />
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
