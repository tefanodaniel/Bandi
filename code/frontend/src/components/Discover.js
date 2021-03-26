import React from 'react';
import {Redirect} from 'react-router-dom';
import Jumbotron from 'react-bootstrap/Jumbotron';
import Button from 'react-bootstrap/Button';
import Cookies from "js-cookie";
import Header from './Header/Header.js'
import {Container, Row, Col} from "react-bootstrap";
import band_img from "../images/band_jumbo.jpg";
import music_img from "../images/music_jumbo.jpg";
import sdate_img from "../images/speeddate_jumbo.jpg";
import sotw_img from "../images/sotw_jumbo.jpg";
import discover_bg from "../images/discover_bg.jpg";

const discover_style = {
	jumbo_music: {
		backgroundAttachment: "static",
		backgroundPosition: "center",
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${music_img})`
	},
	jumbo_band: {
		backgroundAttachment: "static",
		backgroundPosition: "center",
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${band_img})`
	},
	jumbo_sotw: {
		backgroundAttachment: "static",
		backgroundPosition: "center",
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${sotw_img})`
	},
	jumbo_sdate: {
		backgroundAttachment: "static",
		backgroundPosition: "center",
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${sdate_img})`
	}

}

class Discover extends React.Component {
  constructor(props) {
    super(props)

	  this.state = {
  		id : '',
  		u_id : null,
  		first_view : true
	  }
  }

  viewMusicians = () => { this.props.history.push('/musicianview');}

  viewBands = () => {this.props.history.push('/bandview')}

  viewSpeedDating = () => {}

  viewSOTW = () => {this.props.history.push('/sotw')}

  render() {
	  let cookie_id = Cookies.get('id');
	  console.log('are the cookies already set?', cookie_id);
	  if (!cookie_id) { // not logged in or cookie got deleted OR first login so redirect
		  const params = new URLSearchParams(window.location.search);
		  let user_id = params.get("id");
		  console.log('the userid from url params', user_id);
		  // id is in url
		  if (user_id) {
		  	  // so first log in
			  // store id as a cookie
			  Cookies.set('id', user_id);
			  // remove id from url
			  window.history.replaceState(null, '', '/')
		  }
		  else {
		  	  // either beyond first login or unsuccesful login
		  	  //shouldn't be here! so safe to redirect
			  console.log('redirecting since no cookie_id or user_id ');
			  return (<Redirect to = '/signin'/>);
		  }
	  }
	return (
  		<div style={{backgroundImage:`url(${discover_bg})`, height: "1000px",backgroundPosition: "center",backgroundSize: "cover"}}>
        	<Header />
        	<div>
        	<Container >
				<Row style={{ marginTop:"20px"}}>
					<Col style={{ width: "200px", height:"100px"}}>
						<Jumbotron className="rounded text-white" style={discover_style.jumbo_music}>
							<Container style={{float:"right"}}>
								<h3 className="display-5" >Musicians</h3>

							</Container>
							<Container>
								<Button onClick={this.viewMusicians} variant="light" >Browse</Button>
							</Container>
						</Jumbotron>
					</Col>
					<Col style={{ width: "200px", height:"100px"}}>
						<Jumbotron className="rounded text-white" style={discover_style.jumbo_band}>
							<h3>Bands</h3>
							<Button onClick={this.viewBands} variant="light">Browse</Button>
						</Jumbotron>
					</Col>
				</Row>
			</Container>
			</div>
			<div style={{marginTop:"120px"}}>
			<Container >
				<Row style={{ marginTop:"20px"}}>
					<Col style={{ width: "200px", height:"100px"}}>
						<Jumbotron className="rounded text-white" style={discover_style.jumbo_sdate}>
							<Container style={{float:"right"}}>
								<h3 className="display-5" >Speed-Dating</h3>

							</Container>
							<Container>
								<Button onClick={this.viewSpeedDating} variant="light" >Explore</Button>
							</Container>
						</Jumbotron>
					</Col>
					<Col style={{ width: "200px", height:"100px"}}>
						<Jumbotron className="rounded text-white" style={discover_style.jumbo_sotw}>
							<h3>Song Of The Week!</h3>
							<Button onClick={this.viewSOTW} variant="light">Explore</Button>
						</Jumbotron>
					</Col>
				</Row>
			</Container>
			</div>
  		</div>
  	);
  }

}

export default Discover;
