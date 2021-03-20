import React from 'react';
import { withRouter } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image';
import Navbar from 'react-bootstrap/Navbar';
import { logout } from "../../utils/api"; // logout is named export, needs brackets

class Header extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      authenticated: props.authenticated,
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
          <p>Hi, {this.props.name}</p>
          <Button onClick={this.handleLogOut}>Log out</Button>
        </div>
      );
    }
    return (<p>Guest</p>)
  }

  goToProfile = () => { this.props.history.push('/myprofile');}
        
  render() {
  	return (
  		<Navbar>

        <h1>bandi</h1>

  			{this.renderLogin()}

        <Button onClick={this.goToProfile}>My Profile</Button>
			  <Button onClick={() => { logout(); this.props.history.push('/');}}>Log Out</Button>

  		</Navbar>
  	);
  }

}

export default withRouter(Header);
