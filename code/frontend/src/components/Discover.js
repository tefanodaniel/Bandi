import React from 'react';
import {Redirect} from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import Cookies from "js-cookie";
import Header from './Header';
import {Container, Row, Col} from "react-bootstrap";
import { bandi_styles } from "../styles/bandi_styles";
import SubHeader from "./SubHeader";

import { CometChat } from "@cometchat-pro/chat";
import config from '../config';
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

    this.setCookieOnLogin = this.setCookieOnLogin.bind(this);
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

  componentDidUpdate(prevProps) {
    let userName = this.props.store.user_reducer.name
    if (userName !== prevProps.store.user_reducer.name) {
      console.log("UPDATING COMPONENT")
      let chatInitialized = this.props.store.chat_reducer.initialized;
      let loggedIntoChat = this.props.store.chat_reducer.loggedIn;
      if (chatInitialized && !loggedIntoChat) {
        this.props.chatLogin(Cookies.get('id'), userName);
      }
    }
  }


  render() {
    this.setCookieOnLogin()
    if (!Cookies.get('id')) {
      console.log('redirecting since no cookie_id or user_id ');
      return (<Redirect to = '/signin'/>);
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
