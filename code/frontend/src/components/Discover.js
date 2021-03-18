import React from 'react';
import { Redirect } from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import {findCookie, getBackendURL, getFrontendURL, getURL, logout} from "../utils/api";
import axios from "axios";

class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
		  id: ''
	  }
  }

  render() {

	  let url = getBackendURL() + "/id";
	  axios.get(url)
		  .then((response) => this.setState({id: response.data.id}));

	  if (!this.state.id) {
		  return (<h1>Loading...</h1>);
	  }
	  else if (this.state.id == "LOGGED_OUT") {
		  return (<Redirect to="/signin"/>);
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
