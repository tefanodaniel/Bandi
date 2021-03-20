import React from 'react';
import { withRouter } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image';
import Navbar from 'react-bootstrap/Navbar';
import {getBackendURL, logout} from "../../utils/api"; // logout is named export, needs brackets
import Cookies from "js-cookie";
import axios from "axios";

class Header extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      authenticated: props.authenticated,
      id: '',
      name: '',
      location: '',
      experience: ''
    }

    this.renderLogin = this.renderLogin.bind(this);
    this.handleLogOut = this.handleLogOut.bind(this);

  }

  handleLogOut() {
    this.setState({
      ...this.state,
      authenticated: false
    })
  }

  renderLogin() {
    if (this.state.authenticated) {
      return (
        <div>
          <Image src="profile_placeholder.svg" roundedCircle="true" />
          <p>Welcome, {this.state.name}</p>
          <Button onClick={this.goToProfile}>My Profile</Button>
          <Button onClick={() => { logout(); this.handleLogOut(); this.props.history.push('/');}}>Log Out</Button>
        </div>
      );
    }
    return (<p>Guest</p>)
  }

  goToProfile = () => { this.props.history.push('/myprofile');}
        
  render() {
    let my_id = Cookies.get("id");
    if (my_id) {
      this.state.authenticated = true;
    }

    this.state.id = my_id;
    let userURL = getBackendURL() + "/musicians/" + this.state.id;
    // get the signed-in user's info
    axios.get(userURL)
        .then((response) => this.setState(
            {name: response.data.name, location: response.data.location,
              experience: response.data.experience}));

  	return (
  		<Navbar>

        <h1>bandi</h1>

  			{this.renderLogin()}

  		</Navbar>
  	);
  }

}

export default withRouter(Header);
