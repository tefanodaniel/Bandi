import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import {TabPanel} from "react-tabs";
import Header from "./Header/Header";
import {Container, Navbar} from "react-bootstrap";
import BandApiService from '../utils/BandApiService';

class Profile extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

            my_id : '',

            userId : '',
            name: 'Loading...',
            location: '',
            experience: '',
            instruments: [],
            genres: [],
            links: [],
            friends: [],
        }

        this.addFriend.bind(this)


        const params = new URLSearchParams(this.props.location.search);
        this.state.us_id = params.get("view");
        this.state.my_id = Cookies.get("id");

        var userURL = getBackendURL() + "/musicians/" + this.state.us_id;
        axios.get(userURL)
            .then((response) => {
                this.state =
                    {name: response.data.name, location: response.data.location,
                        experience: response.data.experience,
                        instruments: response.data.instruments,
                        genres: response.data.genres,
                        links: response.data.profileLinks,
                        friends: response.data.friends
                    }
            });
    }

    goBack = () => {this.props.history.goBack()};


    addFriend = () => {
        BandApiService.sendFriendRequest(this.state.my_id, this.state.us_id).then((response) =>
            alert("A request to connect was sent to " + this.state.name + ".")
        );
    }

    renderConnectButton = () => {
        // remove question mark once pending_outgoing_requests confirmed to exist
        /*
        if (this.state.pending_outgoing_requests?.indexOf(this.state.userId) == -1) {
            return <Button variant="success" onClick={this.addFriend}>Connect!</Button>
        } else { return <Button disabled>Pending...</Button> };*/
        if (true) {
            return <Button variant="success" onClick={this.addFriend}>Connect!</Button>
        } else { return <Button disabled>Pending...</Button> };
    }

    render() {

        if (this.state.name) {
            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Learn more about fellow Musicians!
                        </Navbar.Brand>
                    </Navbar>
                    <h2>Name: {this.state.name}</h2>
                    <h4>Location: {this.state.location}</h4>
                    <h4>Experience: {this.state.experience}</h4>
                    <div>
                        <h4>Instruments: {this.state.instruments.join(", ")}</h4>
                    </div>
                    <div>
                        <h4>Genres: {this.state.genres.join(", ")}</h4>
                    </div>
                    <div>
                        <h4>Links: {this.state.links.map((link, i) => <a href={link}>{link}</a>)}</h4>
                    </div>
                    {this.renderConnectButton()}
                </div>
            );
        } else {

            return (
                <div>
                    <h1>View a Musician's Profile</h1>
                    <h3>Loading...</h3>
                    <Button onClick={() => {this.goBack()}}>Go Back</Button>
                </div>

            );
        }
    }

}

export default Profile;
