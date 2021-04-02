import React from 'react';
import {Redirect} from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import Cookies from "js-cookie";
import Header from './Header'
import {Container, Row, Col} from "react-bootstrap";
import { bandi_styles } from "../styles/bandi_styles";
import SubHeader from "./SubHeader";

import { CometChat } from "@cometchat-pro/chat"
import config from '../config';
import ChatApi from "../utils/ChatApiService";
import { connect } from 'react-redux';
import { chatLogin } from '../actions/chat_actions';


class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
  		id : '',
  		u_id : null,
  		first_view : true,
	  }

    this.loginInProgress = false;

    this.setCookieOnLogin = this.setCookieOnLogin.bind(this);
    this.createCometChatUser = this.createCometChatUser.bind(this);
    this.logInCometChatUser = this.logInCometChatUser.bind(this);
    this.doChatLogin = this.doChatLogin.bind(this);
  }

  viewMusicians = () => { this.props.history.push('/musicianview');}

  viewBands = () => {this.props.history.push('/bandview')}

  viewSpeedDating = () => {this.props.history.push('/speeddate')} // To Do.. implement a speed-dating component that lets users browse events and register for them.

  viewSOTW = () => {this.props.history.push('/sotw')}

  setCookieOnLogin() {
    let cookie_id = Cookies.get('id');
	  console.log('are the cookies already set?', cookie_id);
    if (!cookie_id) { // not logged in or cookie got deleted OR first login so redirect
      const params = new URLSearchParams(window.location.search);
      let user_id = params.get("id");
      console.log('the userid from url params', user_id);
      // id is in url
      if (user_id) {
         // so first log in
        // store id as a cookie
        Cookies.set('id', user_id);

        // remove id from url
        window.history.replaceState(null, '', '/')
      }
    }
  }

  createCometChatUser(uid, name) {
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

  logInCometChatUser(userAuthToken) {
    console.log("USING AUTH: ", userAuthToken);
    CometChat.login(userAuthToken).then(
      user => {
        console.log("Chat login was successful: ", Cookies.get('id'));
        // Update redux state
        this.props.chatLogin(user)
      },
      error => {
        console.log("Login failed with exception:", { error });
      }
    );
  }

  doChatLogin() {
    let userId = Cookies.get('id');
    ChatApi.accountExists(userId).then((res) => {
        // Check if account exists, and if so, delete any existing auth tokens
        // If no account exists, create one
        if (res) {
          console.log("Found existing account for this user");
          ChatApi.hasAuthTokens(userId).then((res) => {
            console.log("HAS AUTH TOKENS: ", res);
            // If there are previous auth tokens, delete them
            if (res > 0) {
              ChatApi.deleteAllUserAuthTokens(userId).then((res) => {
                if (!res.data.data.success) {
                  console.log("Encountered error while flushing auth tokens", res);
                } else {
                  console.log("Successfully flushed existing auth tokens", res);
                }
              })
            }
          });
        } else {
          // Create account for this user
          this.createCometChatUser(userId, this.props.store.user_reducer.name);
        }

        // Create new auth token for this user
        ChatApi.createUserAuthToken(userId).then((res) => {
          // Finally, log the user in
          setTimeout(() => {this.logInCometChatUser(res.data.data.authToken);}, 1000);
        });
    });
  }

  // componentDidMount() {
  //   this.setCookieOnLogin();
  //   let userId = Cookies.get('id');
  //   let chatInitialized = this.props.store.chat_reducer.initialized;
  //   console.log("ON MOUNT, ", this.props.store);
    // let loggedIntoChat = this.props.store.chat_reducer.loggedIn;
    // console.log("LOOK HERE: ", chatInitialized, this.props.store.chat_reducer.loggedIn);
    // if (userId && chatInitialized && !loggedIntoChat) {
    //   // Log in to chat
    //   // ChatApi
    //   //   .getCurrentUser(userId)
    //   //   .then((res) => {
    //   //     this.setState({chatUserData: res.data.data});
    //   //     Cookies.set("loggedIntoChat", true);
    //   //   }).catch((error) => {
    //   //     // If UID not found, need to create chat account for this user
    //   //     let errorCode = error.response.data.error.code;
    //   //     if (errorCode === "ERR_UID_NOT_FOUND") {
    //   //       this.createCometChatUser(userId, this.props.store.user_reducer.name);
    //   //       Cookies.set("loggedIntoChat", true);
    //   //       return;
    //   //     } else {
    //   //       Cookies.set("loggedIntoChat", false);
    //   //       return error;
    //   //     }
    //   //   });
    //   ChatApi.accountExists(userId).then((res) => {
    //       // Check if account exists, and if so, delete any existing auth tokens
    //       // If no account exists, create one
    //       if (res) {
    //         console.log("Found existing account for this user");
    //         //this.setState({ chatUserData: res });
    //
    //         ChatApi.hasAuthTokens(userId).then((res) => {
    //           console.log("HAS AUTH TOKENS: ", res);
    //           // If there are previous auth tokens, delete them
    //           if (res > 0) {
    //             ChatApi.deleteAllUserAuthTokens(userId).then((res) => {
    //               if (!res.data.data.success) {
    //                 console.log("Encountered error while flushing auth tokens", res);
    //               }
    //             })
    //           }
    //         });
    //       } else {
    //         // Create account for this user
    //         this.createCometChatUser(userId, this.props.store.user_reducer.name);
    //       }
    //
    //       // Create new auth token for this user
    //       ChatApi.createUserAuthToken(userId).then((res) => {
    //         //this.setState({ chatUserAuthToken: res.data.data.authToken });
    //
    //         // Log the user in
    //         this.logInCometChatUser(res.data.data.authToken);
    //       });
    //
    //
    //
    //   });
    //
    //
    //
    //   //   // TODO: log in and set cookie to reflect that
    //   //
    //   //   ChatApi.deleteAllUserAuthTokens(userId).then((res) => {
    //   //     if (res.data.success) {
    //   //       console.log("Flushed all previous auth tokens");
    //   //       ChatApi.createUserAuthToken(userId).then((res) => {
    //   //         console.log("Generated new user auth token");
    //   //         this.setState({ userChatAuthToken: res.data.authToken });
    //   //       }).catch((error) => {
    //   //         console.log("Encountered error while creating new user auth token", error);
    //   //       });
    //   //     }
    //   //   }
    //   // ).catch((error) => {
    //   //   console.log("Encounted error while flushing user auth tokens", error);
    //   // });
    // }
    // if (this.props.store?.chat_reducer) {
    //   console.log("Successfully logged user into chat");
    // }

  //}

  render() {
    this.setCookieOnLogin()
    if (!Cookies.get('id')) {
      console.log('redirecting since no cookie_id or user_id ');
      return (<Redirect to = '/signin'/>);
    }

    let chatInitialized = this.props.store.chat_reducer.initialized;
    let loggedIntoChat = this.props.store.chat_reducer.loggedIn;

    if (chatInitialized && !loggedIntoChat) {
      if (!this.loginInProgress) {
        this.loginInProgress = true;
        console.log("TRYING TO LOGIN TO CHAT");
        //this.props.chatLogin({test: "test_data"})
        this.doChatLogin();
      }
    }

	  return (
  		<div style={bandi_styles.discover_background}>
        	<Header />
        	<SubHeader text={"We need a banDi tagline to insert here"}/>
        	<div style={{marginTop:"120px"}}>
        	<Container >
				<Row>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white" style={bandi_styles.jumbo_music}>
							<Container style={{float:"right"}}>
								<h3 className="display-5" >Musicians</h3>

							</Container>
							<Container>
								<Button onClick={this.viewMusicians} variant="light" >Browse</Button>
							</Container>
						</Jumbotron>
					</Col>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white" style={bandi_styles.jumbo_band}>
							<h3>Bands</h3>
							<Button onClick={this.viewBands} variant="light">Browse</Button>
						</Jumbotron>
					</Col>
				</Row>
			</Container>
			</div>
			<div style={{marginTop:"220px"}}>
			<Container >
				<Row style={{marginTop:"120px"}}>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white" style={bandi_styles.jumbo_sdate}>
							<Container style={{float:"right"}}>
								<h3 className="display-5" >Speed-Dating</h3>

							</Container>
							<Container>
								<Button onClick={this.viewSpeedDating} variant="light" >Explore</Button>
							</Container>
						</Jumbotron>
					</Col>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white" style={bandi_styles.jumbo_sotw}>
							<h3>Song Of The Week!</h3>
							<Button onClick={this.viewSOTW} variant="light">Explore</Button>
						</Jumbotron>
					</Col>
				</Row>
			</Container>
			</div>
		</div>
  	);
  }

}

//export default Discover;

function mapStateToProps(state) {
  return {
    store: state
  };
} // end mapStateToProps
export default connect(mapStateToProps, { chatLogin })(Discover);
