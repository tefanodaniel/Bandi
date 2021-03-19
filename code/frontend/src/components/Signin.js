import React from 'react';

import Button from 'react-bootstrap/Button';
import axios from "axios";
import {getBackendURL} from "../utils/api";


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
          <h1>Bandi</h1>
          <h2> Get started: </h2>
          <Button onClick={() => window.location=url}>Log in with Spotify</Button>

        </div>
    );
  }
}

export default Signin;
