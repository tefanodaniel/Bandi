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

class SpeedDate extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            id: Cookies.get('id'),
            sdEvents: [],

            name: '',
            link: '',
            date: '',
            minusers: 1
        }

        //this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        let value = target.value;
        const field = target.name;
        this.setState({
            [field]: value
        });
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

                    <h1>Create Event: (Admin)</h1>

                    <h1>{this.state.name}{this.state.link}{this.state.date}{this.state.minusers}</h1>

                    <Form onSubmit={this.handleSubmit}>

                        <Form.Group controlId="profileForm.name">
                            <Form.Label>Name:</Form.Label>
                            <Form.Control name="name" type="input"  placeholder="Name" value={this.state.name} onChange={this.handleChange} />
                        </Form.Group>

                        <Form.Group controlId="profileForm.link">
                            <Form.Label>Link:</Form.Label>
                            <Form.Control name="link" type="input"  placeholder="Link" value={this.state.link} onChange={this.handleChange} />
                        </Form.Group>

                        <Form.Group controlId="profileForm.date">
                            <Form.Label>Date:</Form.Label>
                            <Form.Control name="date" type="input"  placeholder="Date" value={this.state.date} onChange={this.handleChange} />
                        </Form.Group>

                        <Form.Group controlId="profileForm.minusers">
                            <Form.Label>Minimum Number of Participants:</Form.Label>
                            <Form.Control name="minusers" type="number"  placeholder="1" value={this.state.minusers} onChange={this.handleChange} />
                        </Form.Group>

                        <Button type="submit">Create Event</Button>

                    </Form>

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