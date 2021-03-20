import React from 'react';
import {Redirect, useHistory} from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import {findCookie, getBackendURL, getFrontendURL, getURL, logout} from "../utils/api";
import axios from "axios";
import Cookies from "js-cookie";
import Header from "./Header/Header";

class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
		id : ''
	  }
  }

	render() {

  	// Get id from query params
  	const params = new URLSearchParams(window.location.search);
  	let my_id = params.get("id");

  	// id is in url
	if (my_id) {
		// store id as a cookie
		Cookies.set('id', my_id);
		// remove id from url
		window.history.replaceState(null, '', '/')
	}

	let cookie_id = Cookies.get('id');
  	if (!cookie_id) { // not logged in
  		return(<Redirect to ='/signin'/>);
	}

  	return (
  		<div>
        <Header></Header>
  			<Jumbotron>
  				<h3>Musicians</h3>
  				<Button>Browse</Button>

				<h3>Bands</h3>
				<Button>Browse</Button>
  			</Jumbotron>

  		</div>
  	);
  }

}

export default Discover;
