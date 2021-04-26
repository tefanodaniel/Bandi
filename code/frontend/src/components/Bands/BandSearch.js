import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import BandSearchResults from "./BandSearchResults";
import BandSearchControls from "./BandSearchControls";
import Header from "../Header/Header";
import SubHeader from "../Header/SubHeader";
import {bandi_styles} from "../../styles/bandi_styles";

class BandSearch extends Component {
    contructor(props) {
        super(props);
    }

    render() {
        if(true) {
            return (
                <div style={bandi_styles.discover_background}>
                    <Header/>
                    <SubHeader text={"Find your future band here!"}/>
                    <Container className="mx-auto" fluid>
                        <Row>
                            <Col className="col-sm-3">
                                <BandSearchControls />
                            </Col>
                            <Col className="col-sm-9">
                                <BandSearchResults />
                            </Col>
                        </Row>
                    </Container>
                    <div></div>
                </div>
            );
        } else {
            return (
                <div>
                    <h1> Here are your search results: </h1>
                    <h3> Loading...</h3>
                </div>
            );
        }
    }
}

function mapStateToProps(state) {
    return {state};
}

export default connect(mapStateToProps)(BandSearch);