import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import {logout} from "../utils/api";
import {connect} from 'react-redux';
import {Container, Row, Col, Navbar, Nav} from "react-bootstrap";
import MusicianList from "./MusicianList";
import MusicianSearch from "./MusicianSearch";
import Header from "./Header";

class SearchMusician extends Component {
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
                    <Container>
                        <Row>
                            <Col>
                                <MusicianSearch />
                            </Col>
                            <Col>
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
