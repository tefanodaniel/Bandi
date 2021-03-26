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

const selectUserData = (state) => {
  return state.user_reducer.user//.find((it) => it.id === id)
}

const Header = () => {
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

  return (
      <div>
        <Navbar expand="lg" variant="dark" bg="dark">
          <Navbar.Brand href="/">
              banDi
          </Navbar.Brand>
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


export default withRouter(Header);
