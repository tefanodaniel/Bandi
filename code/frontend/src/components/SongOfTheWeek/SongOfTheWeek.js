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

import '../../styles/sotw_styles.css';

class SongOfTheWeek extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        return (
                <div style={bandi_styles.discover_background}>
                    <Header/>
                    <SubHeader text={"Participate in our weekly Song Of The Week event!"}/>
                    <div class="sotw-view">
                        <div class="bandi-box sotw-controller-container">
                            <SotwSearchControls />
                        </div>
                        <div class="bandi-box-right-of-calendar">
                            <div class="bandi-box sotw-event-description-container">
                                <SotwEventDesc />
                            </div>
                            <div class="bandi-box sotw-user-submission-container">
                                <SotwUserSubmission />
                            </div>
                        </div>
                        <div class="bandi-box sotw-submission-list-container">
                            <SotwSubmissionList />
                        </div>
                    </div>

                </div>
        );
    }
}

function mapStateToProps(state) {
    return {state};
}

export default connect(mapStateToProps)(SongOfTheWeek);
