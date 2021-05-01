import React from 'react';
import {Redirect} from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import Cookies from "js-cookie";
import Header from './Header/Header';
import {Container, Row, Col} from "react-bootstrap";
import { bandi_styles } from "../styles/bandi_styles";
import SubHeader from "./Header/SubHeader";
import Footer from "./Footer";

import { connect } from 'react-redux';
import { chatLogin } from '../actions/chat_actions';
import {getUser} from "../actions/user_actions";
import { fetchUserBands } from "../actions/user_actions";
import { getIncomingFriendRequests, getOutgoingFriendRequests, getUserFriends } from '../actions/friend_actions';


class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
  		id : '',
  		u_id : null,
  		first_view : true,
	  }
  }

  viewMusicians = () => { this.props.history.push('/musiciansearch');}

  viewBands = () => {this.props.history.push('/bandview')}

  viewSpeedDating = () => {this.props.history.push('/speeddate')} // To Do.. implement a speed-dating component that lets users browse events and register for them.

  viewSOTW = () => {this.props.history.push('/sotw')}

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
		  this.props.getUser(id1).then(() => console.log('Got user info'));
	  }
	  // Load friends and bands for usage throughout the rest of app
	  this.props.fetchFriends(id1)
      .then(() => this.props.fetchIncoming(id1))
      .then(() => this.props.fetchOutgoing(id1))
      .then(() => this.props.fetchBands(id1))
      //.then(() => console.log("Successfully fetched friends, incoming/outgoing requests, and bands"));
      //.catch((err) => console.log(err));
  }

	render() {

    if (!Cookies.get('id')) {
        //console.log('redirecting since no cookie_id or user_id ');
        return (<Redirect to='/signin'/>);
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
        fetchBands: (userID) => dispatch(fetchUserBands(userID))
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Discover);
