import React from 'react';
import { Redirect } from 'react-router-dom';

import Header from '../components/Header/Header';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';

import queryString from 'query-string';
import {findCookie, getFrontendURL, getURL} from "../utils/api";

class Discover extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    // Use cookie to see if logged in
	let userID = findCookie("id");
	if (!userID) {
		return (<Redirect to="/signin"/>);
	}

	const url = getFrontendURL();
	const profile_redirect = url + "/profile";

  	return (
  		<div>

            <header></header>
			<Button onClick={() => { this.props.history.push('/profile');}}>My Profile</Button>

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
