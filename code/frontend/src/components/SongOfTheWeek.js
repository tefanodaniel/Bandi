import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import {logout} from "../utils/api";
import {connect} from 'react-redux';
import {Container, Row, Col, Navbar, Nav} from "react-bootstrap";
import SoTWsearch from "./SoTWsearch";
import SoTWDesc from "./SoTWDesc";
import SoTWSubmissions from "./SoTWSubmissions";
import Header from "./Header/Header";

class SongOfTheWeek extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        if(true) {
            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Find your musical soulmate here!
                        </Navbar.Brand>
                    </Navbar>
                    <Container className="mx-auto" fluid>
                        <Row>
                            <Col className="col-sm-3">
                                <SoTWsearch />
                            </Col>
                            <Col className="col-sm-9">
                                <SoTWDesc /> //this should have a submit button.
                                <SoTWSubmissions /> //this should have a list of all filtered submissions (by cycle, genre, instrument) with pagination.
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

export default connect(mapStateToProps)(SongOfTheWeek);

