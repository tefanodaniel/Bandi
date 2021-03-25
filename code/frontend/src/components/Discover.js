import React from 'react';
import {Link, Redirect, useHistory} from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import {findCookie, getBackendURL, getFrontendURL, getURL, logout} from "../utils/api";
import axios from "axios";
import Cookies from "js-cookie";

import Header from './Header'
import {Container} from "react-bootstrap";
import MusicianSearch from "./MusicianSearch";
import { getUser } from "../actions/user_actions";
import {shallowEqual, useSelector} from "react-redux";

class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
  		id : '',
  		u_id : null,
  		first_view : true
	  }
  }

  viewMusicians = () => { this.props.history.push('/musicianview');}

  viewBands = () => {this.props.history.push('/bandview')}

  render() {
	  let cookie_id = Cookies.get('id');
	  console.log('are the cookies already set?', cookie_id);
	  if (!cookie_id) { // not logged in or cookie got deleted OR first login so redirect
		  //this.setState({
		  //	...this.state,
		  //	u_id : null,
		  //	first_view : true
		  //})
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
			  //getUser(user_id)
		  }
		  else {
		  	  // either beyond first login or unsuccesful login
		  	  //shouldn't be here! so safe to redirect
			  //this.setState({
			  //	...this.state,
			  //	u_id : null,
			  //	first_view : true
			  //})
			  console.log('redirecting since no cookie_id or user_id ');
			  return (<Redirect to = '/signin'/>);
		  }
		  //console.log('redirecting since no cookie');
		  //return(<Redirect to ='/signin'/>);
	  }

	return (
  		<div>
        	<Header key={cookie_id} id={cookie_id} />
			<Jumbotron>
  				<h3>Musicians</h3>
  				<Button onClick={this.viewMusicians}>Browse</Button>

				<h3>Bands</h3>
				<Button onClick={this.viewBands}>Browse</Button>
  			</Jumbotron>

  		</div>
  	);
  }

}

export default Discover;
