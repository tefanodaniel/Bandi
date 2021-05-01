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
                <div class="sotw-outer">
                    <Header/>
                    <div class="bandi-box sotw-intro">
                        <p id="sotw-intro-text" class="sotw-intro-text">
                            Show off your skills and check out what other people on Bandi are all about! Bandi
                            song of the week is a place for users to share clips of themselves playing popular songs
                            so you can get a better idea of who's doing what on Bandi...
                        </p>
                    </div>
                    <div class="sotw-view">
                        <div class="sotw-controller-container">
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
