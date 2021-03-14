import React from 'react';

import Button from 'react-bootstrap/Button';


class Signin extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    const isDev = !process.env.NODE_ENV || process.env.NODE_ENV === "development";
    const url = isDev ? "http://localhost:4567/login" : "https://bandiscover-api.herokuapp.com/login";

    return (
      <div>
        <h1>Bandi</h1>
        <h2> Sign in: </h2>
        <Button onClick={() => window.location=url}>Log in with Spotify</Button>
        <Button onClick={() => { this.props.history.push('/newprofile');}}>Create profile</Button>

        <Button>Continue as guest (not functional)</Button>
      </div>
    );
  }
}

export default Signin;
