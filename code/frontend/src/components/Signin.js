import React from 'react';
import { Redirect } from 'react-router-dom';
import Cookies from "js-cookie";
import {getBackendURL} from "../utils/api";
import { Nav, Navbar, Image } from "react-bootstrap";
import guitar from "../images/free-guitar-image.jpg"

class Signin extends React.Component {
  constructor(props) {
    super(props)

      this.state = {
          spotifyURL: ''
      }

      this.setCookieOnLogin = this.setCookieOnLogin.bind(this);
  }

  setCookieOnLogin() {
    let cookie_id = Cookies.get('id');
	   //  console.log('are the cookies already set?', cookie_id);
    if (!cookie_id) { // not logged in or cookie got deleted OR first login so redirect
      const params = new URLSearchParams(window.location.search);
      let user_id = params.get("id");
      //console.log('the userid from url params', user_id);
      // id is in url
      if (user_id) {
         // so first log in
        // store id as a cookie
        Cookies.set('id', user_id);

        // remove id from url
        window.history.replaceState(null, '', '/')
      }
    }
  }

  render() {
    const url = getBackendURL() + "/login";

    this.setCookieOnLogin();
    if (Cookies.get("id")) {
      return (<Redirect to="/discover"/>);
    }


    return (
        <div>
            <Navbar expand="lg" variant="light" bg="light">
                <Navbar.Brand href="/">banDi</Navbar.Brand>
                <Nav className="mr-auto">
                    <Nav.Link onClick={() => window.location=url}>Log In (via Spotify)</Nav.Link>
                </Nav>
            </Navbar>
            <Image src={guitar} />
        </div>
    );
  }
}

export default Signin;
