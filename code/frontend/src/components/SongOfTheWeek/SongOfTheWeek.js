import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import SotwSearchControls from "./SotwSearchControls";
import SotwEventDesc from "./SotwEventDesc";
import SotwSubmissionList from "./SotwSubmissionList";
import Header from "../Header/Header";
import SubHeader from "../Header/SubHeader";
import {bandi_styles} from "../../styles/bandi_styles"
import SotwUserSubmission from "./SotwUserSubmission";

class SongOfTheWeek extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        return (
                <div style={bandi_styles.discover_background}>
                    <Header/>
                    <SubHeader text={"Participate in our weekly Song Of The Week event!"}/>
                    <Container className="mx-auto" fluid>
                        <Row>
                            <Col className="col-sm-3">
                                <SotwSearchControls />
                            </Col>
                            <Col className="col-sm-9 bg-transparent">
                                <Row>
                                    <Col className="col-sm-6 bg-transparent">
                                        <SotwEventDesc />
                                    </Col>
                                    <Col className="col-sm-6 bg-transparent">
                                        <SotwUserSubmission />
                                    </Col>
                                </Row>
                                <Row>
                                <SotwSubmissionList />
                                </Row>
                            </Col>
                        </Row>
                    </Container>
                    <div></div>

                </div>
        );
    }
}

function mapStateToProps(state) {
    return {state};
}

export default connect(mapStateToProps)(SongOfTheWeek);
