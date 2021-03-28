import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import {logout} from "../utils/api";
import {connect} from 'react-redux';
import {Container, Row, Col, Navbar, Nav} from "react-bootstrap";
import MusicianList from "./MusicianList";
import MusicianSearch from "../features/browse/MusicianSearch";
import Header from "./Header/Header";
import discover_bg from "../images/discover_bg.jpg";

class SearchMusician extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        if(true) {
            return (
                <div style={{backgroundImage:`url(${discover_bg})`, height: "1000px",backgroundPosition: "center",backgroundSize: "cover"}}>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Find your musical soulmate here!
                        </Navbar.Brand>
                    </Navbar>
                    <Container className="mx-auto" fluid>
                        <Row>
                            <Col className="col-sm-3">
                                <MusicianSearch />
                            </Col>
                            <Col className="col-sm-9">
                                <MusicianList />
                            </Col>
                        </Row>
                    </Container>
                    <div></div>

                </div>
            );
        }
        else {
            return (
                <div>
                    <h1> Here is your search! </h1>
                    <h3> Loading ... </h3>

                </div>
            );
        }
    }
}

function mapStateToProps(state) {
    return {state};
}

export default connect(mapStateToProps)(SearchMusician);

