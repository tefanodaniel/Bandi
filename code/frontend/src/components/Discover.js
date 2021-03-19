import React from 'react';
import { Redirect } from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import {findCookie, getBackendURL, getFrontendURL, getURL, logout} from "../utils/api";
import axios from "axios";
import Cookies from "js-cookie";

class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
		id : ''
	  }
  }

	render() {

  	// Get id from search params (not ideal, don't want search params in url)
  	const params = new URLSearchParams(window.location.search);
  	let my_id = params.get("id");
	if (my_id) {
		Cookies.set('id', my_id);
	}

	let cookie_id = Cookies.get('id');
  	if (!cookie_id) { // not logged in
  		return(<Redirect to ='/signin'/>);
	}

  	return (
  		<div>

            <header></header>
			<Button onClick={() => { this.props.history.push('/profile');}}>My Profile</Button>
			<Button onClick={() => { logout(); this.props.history.push('/signin');}}>Log Out</Button>

  			<Jumbotron>
  				<h3>Musicians</h3>
  				<Button>Browse</Button>
  			</Jumbotron>

  			<h3>Bands</h3>

  		</div>
  	);
  }

}

export default Discover;
