import React from 'react';

import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image';
import Navbar from 'react-bootstrap/Navbar';


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


  render() {
  	return (
  		<Navbar>
        <Button>Burger</Button>

        <h1>bandi</h1>

  			{this.renderLogin()}

  		</Navbar>
  	);
  }

}

export default Header;
