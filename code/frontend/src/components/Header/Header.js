import React from 'react';
import { withRouter } from 'react-router-dom';
import Navbar from 'react-bootstrap/Navbar';
import { logout } from "../../utils/api"; // logout is named export, needs brackets
import { Nav, NavDropdown } from "react-bootstrap";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import { USER_LOGOUT, CHAT_LOGOUT } from "../../actions/types";
import { chatLogout } from "../../actions/chat_actions";
import styles from "../../styles/navDropdown.module.css";

const Header = () => {
  const dispatch = useDispatch();
  let user = useSelector((state) => state.user_reducer, shallowEqual);


  const handleLogout = () => {
    // Log out of chat
    chatLogout();

    // Log out of app
    logout()
    dispatch({
      type: USER_LOGOUT
    })
  }

  if(user !== undefined ) {
    return (
        <div>
          <Navbar expand="lg" variant="dark" bg="dark">
            <Navbar.Brand href="/">
              banDi
            </Navbar.Brand>
            <Navbar.Brand className="mx-auto">
              Welcome, {user.name}!
            </Navbar.Brand>

            <div className={styles.navDropdownTitle}>
              <NavDropdown title="My Account">
                <div className={styles.navDropdownItems}>
                  <NavDropdown.Item href="#myprofile">Profile</NavDropdown.Item>
                  <NavDropdown.Item href="#mychats">Chats</NavDropdown.Item>
                  <NavDropdown.Item href="#signin" onClick={handleLogout}>Log Out</NavDropdown.Item>
                </div>
              </NavDropdown>
            </div>
          </Navbar>
        </div>
    )
  } else { //Header edge case that should never happen
    return (
        <div>
          <Navbar expand="lg" variant="dark" bg="dark">
            <Navbar.Brand href="/">
              banDi
            </Navbar.Brand>
            <Navbar.Brand className="mx-auto">
              Welcome!
            </Navbar.Brand>

            <div className={styles.navDropdownTitle}>
              <NavDropdown title="My Account">
                <div className={styles.navDropdownItems}>
                  <NavDropdown.Item href="#myprofile">Profile</NavDropdown.Item>
                  <NavDropdown.Item href="#mychats">Chats</NavDropdown.Item>
                  <NavDropdown.Item href="#signin" onClick={handleLogout}>Log Out</NavDropdown.Item>
                </div>
              </NavDropdown>
            </div>
          </Navbar>
        </div>
    )
  }


}

// const DropdownTitle = () => {
//   return ();
// }


export default withRouter(Header);
