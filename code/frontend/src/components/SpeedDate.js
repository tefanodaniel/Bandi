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

class SpeedDate extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            id: Cookies.get('id'),
            sdEvents: []
        }
    }

    componentDidMount() {
        SDEventApi.getAll()
            .then((response) =>
            this.setState({sdEvents : response.data}));
    }

    render() {
        console.log(this.state.sdEvents);

        const userInfo = this.props.store.user_reducer;
        const isAdmin = userInfo.admin;
        console.log('here here', userInfo);

        var eventList = this.state.sdEvents.map((event) =>
            <div className="card">
                <div className="card-body">
                    <h5 className="card-title">{event.name}</h5>
                    <h6 className="card-subtitle">{event.date}</h6>
                    <h6 className="card-subtitle"><a href={event.link}>{event.link}</a></h6>
                    <p className="card-text">Minimum number of participants: {event.minusers}</p>
                    <Button className="small font-italic" onClick={() => {}}>Register</Button>
                </div>
            </div>
        );

        if (isAdmin) {
            return(
                <div className="bg-transparent" style={bandi_styles.discover_background}>
                    <Header/>
                    <SubHeader text={"Register for speed-dating events here!"}/>

                    <h1>Speed-Dating Events:</h1>
                    {eventList}

                    <h1>Create Event: (Admin Only)</h1>
                </div>
            )
        }
        else { // not an admin
            return (
                <div className="bg-transparent" style={bandi_styles.discover_background}>
                    <Header/>
                    <SubHeader text={"Register for speed-dating events here!"}/>

                    <h1>Speed-Dating Events:</h1>
                    {eventList}
                </div>
            )
        }
    }

}

function mapStateToProps(state) {
    return {store:state};
}

export default connect(mapStateToProps)(SpeedDate);