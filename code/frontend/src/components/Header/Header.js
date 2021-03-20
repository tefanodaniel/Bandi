import React from 'react';
import {Redirect, withRouter} from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import Image from 'react-bootstrap/Image';
import Navbar from 'react-bootstrap/Navbar';
import {getBackendURL, logout} from "../../utils/api"; // logout is named export, needs brackets
import Cookies from "js-cookie";
import axios from "axios";
import {Container, Nav} from "react-bootstrap";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {getUser} from "../../features/user/UserReducer";
import { Link} from "react-router-dom";

const selectUserData = (state) => {
  return state.user_reducer.user//.find((it) => it.id === id)
}

const Header = (id) => {
  const dispatch = useDispatch();
  const id1 = Cookies.get("id");
  let user = useSelector((state) => state.user_reducer, shallowEqual);
  console.log('first is ', user);
  if(user.length === 0) {
    dispatch(getUser(id1))
  }
  user = useSelector((state) => state.user_reducer, shallowEqual);
  console.log('user is ', user)
  const handlelogout = () => {
    logout()
    dispatch({
      type: 'user/logout'
    })
  }
  //const goToProfile = () => { this.props.history.push('/myprofile');}

  return (
      <div>
        <Navbar expand="lg" variant="dark" bg="dark">
          <Navbar.Brand href="/">banDi</Navbar.Brand>
          <Navbar.Brand className="mx-auto">
              Welcome, {user.name}!
          </Navbar.Brand>
            <Nav className="mr-sm-2">
                <Nav.Link href="#myprofile">My Profile</Nav.Link>
            <Nav.Link href="/signin" onClick={handlelogout}>Log Out</Nav.Link>
          </Nav>
        </Navbar>
      </div>
  )
}

/**class Header extends React.Component {
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
  	    <div>
        <Navbar expand="lg" variant="dark" bg="dark">
          <Navbar.Brand href="/">banDi</Navbar.Brand>
          <Nav className="mr-auto">
            <Nav.Link href="/profile">My Profile</Nav.Link>
            <Nav.Link href="/signin" onClick={logout}>Log Out</Nav.Link>
          </Nav>
        </Navbar>
        <Navbar>
        <h1>bandi</h1>
  			{this.renderLogin()}
  		</Navbar>
        </div>
  );
  }

}*/

export default withRouter(Header);
