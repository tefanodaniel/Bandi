import React from 'react';

import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';

class Discover extends React.Component {

  render() {
  	return (
  		<div>
  			<h1>bandi</h1>

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