import React from 'react';
import {bandi_styles} from "../styles/bandi_styles";
import Header from "./Header";
import SubHeader from "./SubHeader";
import Cookies from "js-cookie";

import { connect } from 'react-redux';
import { fetchSDEvents } from '../actions/sd_event_actions';
import SDEventApi from "../utils/SDEventApiService";
import Button from "react-bootstrap/Button";
import {fetchBandsForMusician} from "../actions/band_actions";
import Form from "react-bootstrap/Form";
import FormGroup from "react-bootstrap/FormGroup";
import Jumbotron from "react-bootstrap/Jumbotron";
import BandApi from "../utils/BandApiService";
import MusicianApi from "../utils/MusicianApiService";

class SpeedDateEvent extends React.Component {
    constructor(props) {
        super(props)

        this.state = {

            id: Cookies.get('id'),
            isParticipant: false,
            buttonText: 'Register',

            eventId: '',
            name: 'Loading...',
            link: '',
            date: '',
            minusers: 1,
            participants: [],
            participantNames: []
        }

        this.eventInfo = this.eventInfo.bind(this);
        this.register_leave = this.register_leave.bind(this);
        this.buttonText = this.buttonText.bind(this);
    }

    eventInfo() {
        const params = new URLSearchParams(this.props.location.search);
        let eventId = params.get("view");

        SDEventApi.get(eventId)
            .then((response) =>
                    this.setState({
                        name: response.data.name,
                        eventId: response.data.id,
                        link: response.data.link,
                        date: response.data.date,
                        minusers: response.data.minusers,
                        participants: response.data.participants},

                function() {
                    this.setState({isParticipant :
                            (this.state.participants).indexOf(this.state.id) > -1})
                        /*
                    this.setState({participantNames: []});

                    this.state.participants.forEach(id => {
                        SDEventApi.get(id)
                            .then((response) =>
                                this.setState({participantNames:
                                        this.state.participantNames.concat(response.data.name)})
                            );
                    });*/
                }));
    }

    register_leave() {
        this.setState({isParticipant : !(this.state.isParticipant)});
        this.buttonText();
    }

    buttonText() {
        if (this.state.isParticipant) {
            this.state.buttonText = "Leave";
        }
        else {
            this.state.buttonText = "Register";
        }
    }

    componentDidMount() {
        this.eventInfo();
        this.setState({isParticipant :
                (this.state.participants).indexOf(this.state.id) > -1})
        this.buttonText();
    }

    render() {

        const userInfo = this.props.store.user_reducer;
        const isAdmin = userInfo.admin;
        this.buttonText();

        if (!isAdmin) { // non admin view
            return(
                <div className="bg-transparent" style={bandi_styles.discover_background}>
                    <Header/>

                </div>
            )
        }
        else { // is admin
            return (
                <div className="bg-transparent" style={bandi_styles.discover_background}>
                    <Header/>

                    <h1>{this.state.name} (Admin)</h1>
                    <h2>{this.state.date}</h2>
                    <h2><a href={this.state.link}>{this.state.link}</a></h2>
                    <h2>Minimum number of participants: {this.state.minusers}</h2>
                    <h2>Registered participants: {this.state.participants.length}</h2>

                    <Button onClick={() => {this.register_leave()}}>{this.state.buttonText}</Button>
                    <h1>{this.state.isParticipant.toString()}</h1>


                </div>
            )
        }
    }

}

function mapStateToProps(state) {
    return {store:state};
}

export default connect(mapStateToProps)(SpeedDateEvent);