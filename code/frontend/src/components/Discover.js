import React from 'react';
import {Redirect} from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import Cookies from "js-cookie";
import Header from './Header/Header';
import {Container, Row, Col, Image } from "react-bootstrap";
import { bandi_styles } from "../styles/bandi_styles";
import SubHeader from "./Header/SubHeader";
import {allMusiciansQuery} from "../actions/musician_actions";
import {shallowEqual, useDispatch, useSelector} from "react-redux";

import { CometChat } from "@cometchat-pro/chat";
import config from '../config';
import { connect } from 'react-redux';
import { chatLogin } from '../actions/chat_actions';
import {getUser} from "../actions/user_actions";
import { fetchBandsForMusician } from "../actions/band_actions";
import { getIncomingFriendRequests, getOutgoingFriendRequests, getUserFriends } from '../actions/friend_actions';
import "../styles/discover.css";

class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
  		id : '',
  		u_id : null,
  		first_view : true,
	  }

    this.setCookieOnLogin = this.setCookieOnLogin.bind(this);
  }

  viewMusicians = () => { this.props.history.push('/musiciansearch');}

  viewBands = () => {this.props.history.push('/bandview')}

  viewSpeedDating = () => {this.props.history.push('/speeddate')} // To Do.. implement a speed-dating component that lets users browse events and register for them.

  viewSOTW = () => {this.props.history.push('/sotw')}

  setCookieOnLogin() {
    let cookie_id = Cookies.get('id');
	//  console.log('are the cookies already set?', cookie_id);
    if (!cookie_id) { // not logged in or cookie got deleted OR first login so redirect
      const params = new URLSearchParams(window.location.search);
      let user_id = params.get("id");
      //console.log('the userid from url params', user_id);
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

  componentDidUpdate(prevProps) {
    let userName = this.props.store.user_reducer?.name
    if (userName !== prevProps.store.user_reducer?.name) {
      let chatInitialized = this.props.store.chat_reducer.initialized;
      let loggedIntoChat = this.props.store.chat_reducer.loggedIn;
      if (chatInitialized && !loggedIntoChat) {
        this.props.chatLogin(Cookies.get('id'), userName);
      }
    }
  }
  componentDidMount() {
	  const id1 = Cookies.get("id");
	  let user = this.props.store.user_reducer;
	  if(Object.keys(user).length === 0) {
		  console.log("Not getting yet in componentDidMount")
		  this.props.getUser(id1)
	  }
	  // Load friends and bands for usage throughout the rest of app
	  this.props.fetchFriends(id1);
	  this.props.fetchIncoming(id1);
	  this.props.fetchOutgoing(id1);
	  this.props.fetchBands(id1);
  }


	render() {

    this.setCookieOnLogin()
    if (!Cookies.get('id')) {
        //console.log('redirecting since no cookie_id or user_id ');
        return (<Redirect to='/signin'/>);
	}
	
	return (
  		<div class="outer-discover">
        	<Header />
			<img id="logo" src="../bandi-logo.png"></img>
        	<div>
        	<Container >
				<Row>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white">
							<Container style={{float:"right"}}>
								<h3 className="display-5" >Musicians</h3>

							</Container>
							<Container>
								<Button variant="outline-dark" onClick={this.viewMusicians} >Browse</Button>
							</Container>
						</Jumbotron>
					</Col>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white" >
							<h3>Bands</h3>
							<Button variant="outline-dark" onClick={this.viewBands}>Browse</Button>
						</Jumbotron>
					</Col>
				</Row>
			</Container>
			</div>
			<div>
			<Container >
				<Row style={{marginTop:"120px"}}>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white" >
							<Container style={{float:"right"}}>
								<h3 className="display-5" >Speed-Dating</h3>

							</Container>
							<Container>
								<Button variant="outline-dark" onClick={this.viewSpeedDating}  >Explore</Button>
							</Container>
						</Jumbotron>
					</Col>
					<Col style={bandi_styles.discover_row_col}>
						<Jumbotron className="rounded text-white" >
							<h3>Song Of The Week!</h3>
							<Button variant="outline-dark" onClick={this.viewSOTW} >Explore</Button>
						</Jumbotron>
					</Col>
				</Row>
			</Container>
			</div>
		</div>
  	);
  }

}

function mapStateToProps(state) {
  return {
    store: state
  };
} // end mapStateToProps

function mapDispatchToProps(dispatch) {
    return {
		chatLogin: (id, userName) => dispatch(chatLogin(id, userName)),
		getUser: (id) => dispatch(getUser(id)),
        fetchFriends: (id) => dispatch(getUserFriends(id)),
		fetchIncoming: (id) => dispatch(getIncomingFriendRequests(id)),
		fetchOutgoing: (id) => dispatch(getOutgoingFriendRequests(id)),
        fetchBands: (userID) => dispatch(fetchBandsForMusician({id: userID}))
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Discover);
