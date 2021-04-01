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

            eventId: '',
            name: '',
            link: '',
            date: '',
            minusers: 1,
            participants: []
        }
    }

    componentDidMount() {
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
                    participants: response.data.participants}));
    }

    render() {

        const userInfo = this.props.store.user_reducer;
        const isAdmin = userInfo.admin;
        console.log('here here', userInfo);

        if (isAdmin) {
            return(
                <div className="bg-transparent" style={bandi_styles.discover_background}>
                    <Header/>

                    <h1>{this.state.name}</h1>
                    <h2>{this.state.date}</h2>
                    <h2><a href={this.state.link}>{this.state.link}</a></h2>
                    <h2>Minimum number of participants: {this.state.minusers}</h2>
                    <h2>Registered participants: {this.state.participants.length}</h2>
                </div>
            )
        }
        else { // not an admin
            return (
                <div className="bg-transparent" style={bandi_styles.discover_background}>
                    <Header/>

                    <h1>{this.state.name}</h1>
                </div>
            )
        }
    }

}

function mapStateToProps(state) {
    return {store:state};
}

export default connect(mapStateToProps)(SpeedDateEvent);