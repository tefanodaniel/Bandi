import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import MusicianSearchResults from "./MusicianSearchResults";
import MusicianSearchControls from "./MusicianSearchControls";

import Header from "../Header/Header";
import SubHeader from "../Header/SubHeader";
import {bandi_styles} from "../../styles/bandi_styles";

class MusicianSearch extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        if(true) {
            return (
                <div style={bandi_styles.discover_background}>
                    <Header/>
                    <SubHeader text={"Find your musical soulmate here!"}/>
                    <Container className="mx-auto" fluid>
                        <Row>
                            <Col className="col-sm-3">
                                <MusicianSearchControls />
                            </Col>
                            <Col className="col-sm-9">
                                <MusicianSearchResults />
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
    return {state}
}

export default connect(mapStateToProps)(MusicianSearch);
