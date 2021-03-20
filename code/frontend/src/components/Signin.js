import React from 'react';
import {getBackendURL} from "../utils/api";
import {Nav, Navbar, Image} from "react-bootstrap";
import guitar from "../images/free-guitar-image.jpg"

class Signin extends React.Component {
  constructor(props) {
    super(props)

      this.state = {
          spotifyURL: ''
      }
  }

  render() {
    const url = getBackendURL() +"/login";

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
