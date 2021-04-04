import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import SoTWsearch from "./SoTWsearch";
import SoTWDesc from "./SoTWDesc";
import SoTWSubmissions from "./SoTWSubmissions";
import Header from "./Header";
import SubHeader from "./SubHeader";
import {bandi_styles} from "../styles/bandi_styles"

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
                                <SoTWsearch />
                            </Col>
                            <Col className="col-sm-9 bg-transparent">
                                <SoTWDesc />
                                <SoTWSubmissions />
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

