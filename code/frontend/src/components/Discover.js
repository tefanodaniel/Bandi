import React from 'react';
import { Redirect } from 'react-router-dom';

import Header from '../components/Header/Header';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';

import queryString from 'query-string';

class Discover extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    // Check URL to see if we've been authenticated. Need a better way to do this
    let parsed = queryString.parse(window.location.search);
    console.log(parsed);
    if (!parsed.name) {
      return (<Redirect to="/signin"/>);
    }

  	return (
  		<div>
        <Header authenticated={true} name={parsed.name}/>

  			<Jumbotron>
  				<h3>Musicians</h3>
  				<Button>Browse</Button>
  			</Jumbotron>

  			<h3>Bands</h3>

  		</div>
  	);
  }

}

export default Discover;
