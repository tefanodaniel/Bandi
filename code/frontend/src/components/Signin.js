import React from 'react';

import Button from 'react-bootstrap/Button';


class Signin extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
      <div>
        <h1>Bandi</h1>
        <h2> Sign in: </h2>
        <Button onClick={() => window.location="http://localhost:4567/login"}>Log in with Spotify</Button>
        <Button>Continue as guest (not functional)</Button>
      </div>
    );
  }
}

export default Signin;
