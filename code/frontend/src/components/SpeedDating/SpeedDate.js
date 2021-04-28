import React from 'react';
import {bandi_styles} from "../../styles/bandi_styles";
import Header from "../Header/Header";
import SubHeader from "../Header/SubHeader";
import Cookies from "js-cookie";

import { connect } from 'react-redux';
import { fetchSDEvents } from '../../actions/sd_event_actions';
import SDEventApi from "../../utils/SDEventApiService";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Jumbotron from "react-bootstrap/Jumbotron";
import BandApi from "../../utils/BandApiService";
import Modal from "react-bootstrap/Modal";
import SpeedDateEvent from './SpeedDateEvent.js';

import '../../styles/speed_dating.css';

class SpeedDate extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            id: Cookies.get('id'),
            sdEvents: [],

            name: '',
            link: '',
            date: '',
            minusers: 1,
            showModal: false,
            modalEvent: 0,
            isParticipant: false
        }

        this.handleSubmit = this.handleSubmit.bind(this);
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

    handleSubmit(event) {
        SDEventApi.create({
            name: this.state.name,
            link: this.state.link,
            date: this.state.date,
            minusers: this.state.minusers
        })
            .then(res => {
                console.log(`statusCode: ${res.statusCode}`)
                console.log(res)
            })
            .catch(error => {
                console.error(error)
            })
    }

    componentDidMount() {
        SDEventApi.getAll()
            .then((response) =>
            this.setState({sdEvents : response.data}));
    }

    func(arr) {
        var size;
        if (arr == undefined) {
            size = 0;
        }
        else{
            size = arr.length;
        }
        return size;
    }

    register_leave() {
        if (!this.state.isParticipant) {
            // add participant to event
            SDEventApi.addParticipant(this.state.eventId, this.state.id)
                .then((response) => console.log(response.data));
        }
        else {
            // remove participant from event
            SDEventApi.deleteParticipant(this.state.eventId, this.state.id)
                .then((response) => console.log(response.data));
        }

        this.setState({isParticipant : !(this.state.isParticipant)});
        this.buttonText();
    }

    render_event_button() {
        if (this.state.isParticipant) {
            return <button class="bandi-button event" onClick={() => this.register_leave()}>Leave event</button> 
        } else {
            return <button class="bandi-button event" onClick={() => this.register_leave()}>Register for event</button> 
        }
    }

    render() {
        console.log(this.state);

        const userInfo = this.props.store.user_reducer;
        const isAdmin = userInfo.admin;

        const handleClose = () => this.setState({showModal: false});
        const handleShow = (event) => { 
            this.setState(
                { showModal: true,
                    modalEvent: event,
                    isParticipant: (event.participants).indexOf(this.state.id) > -1
                }) 
        };
        
        var eventList = this.state.sdEvents.map((event) =>

                <Jumbotron className="bandi-card dating-event">
                <div className="bandi-text-fields dating-event">
                    <h5 className="card-title">{event.name}</h5>
                    <h6 className="card-text">{event.date}</h6>
                    <h6 className="card-text"><a href={event.link}>{event.link}</a></h6>
                    <p className="card-text">Minimum number of participants: {event.minusers}</p>
                    <p className="card-text">Registered participants: {this.func(event.participants)}</p>
                    <button className="bandi-button view-sd" onClick={() => {handleShow(event)}}>View More</button>
                </div>
                </Jumbotron>
        );

        if (isAdmin) {
            return(
                <div class="speed-dating-outer">
                    <Header/>
                    <h1>Speed-Dating Events:</h1>
                    <div class="dating-container">
                        {eventList}
                    </div>

                    <Modal className="bandi-modal"show={this.state.showModal} onHide={handleClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>{this.state.modalEvent.name}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <h5>{this.state.modalEvent.date}</h5>
                            <h5><a href={this.state.modalEvent.link}>{this.state.modalEvent.link}</a></h5>
                            <h5>Minimum number of participants: {this.state.modalEvent.minusers}</h5>
                            <h5>Registered participants: {this.func(this.state.modalEvent.participants)}</h5>
                        </Modal.Body>
                        <Modal.Footer>
                            {this.render_event_button()}
                            <button id="close" class="bandi-button" onClick={handleClose}>
                                Close
                            </button>
                        </Modal.Footer>
                    </Modal>

                    <h1>Create Event: (Admin)</h1>

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

                        <button class="bandi-button" type="submit">Create Event</button>

                    </Form>

                </div>
            )
        }
        else { // not an admin
            return (
                <div class="speed-dating-outer">
                    <Header/>
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
