import React from 'react';
import { withRouter } from 'react-router-dom';
import Navbar from 'react-bootstrap/Navbar';
import { logout } from "../utils/api"; // logout is named export, needs brackets
import Cookies from "js-cookie";
import { Nav } from "react-bootstrap";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import { getUser } from "../actions/user_actions";
import { USER_LOGOUT, CHAT_LOGOUT } from "../actions/types";
import { chatLogout } from "../actions/chat_actions";

const selectUserData = (state) => {
  return state.user_reducer.user//.find((it) => it.id === id)
}

const Header = () => {
  const dispatch = useDispatch();
  const id1 = Cookies.get("id");
  let user = useSelector((state) => state.user_reducer, shallowEqual);
  console.log('user is', user)
  if(Object.keys(user).length === 0) {
    console.log("getting user here")
    dispatch(getUser(id1))
  }
  user = useSelector((state) => state.user_reducer, shallowEqual);
  const handleLogout = () => {
    // Log out of chat
    chatLogout();

    // Log out of app
    logout()
    dispatch({
      type: USER_LOGOUT
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
                <Nav.Link href="#mychats">My Chats</Nav.Link>
            <Nav.Link href="/signin" onClick={handleLogout}>Log Out</Nav.Link>
          </Nav>
        </Navbar>
      </div>
  )
}


export default withRouter(Header);
