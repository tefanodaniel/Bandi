import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Cookies from "js-cookie";
import Header from "./Header/Header";
import {Nav, Navbar} from "react-bootstrap";

class Band extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

            userId : '',

            bandId : '',
            bandName : '',
            bandCapacity : 0,
            genres: [],
            members: [],

            isMember: false,
            joinButtonText: 'Join Band'
        }
    }

    goBack = () => {this.props.history.goBack()}

    join_leave() {

        const url = getBackendURL() + "/bands/" + this.state.bandId + "/" + this.state.userId;

        if (this.state.isMember) {
            // put musician in the band
            axios.put(url)
                .then((response) => console.log(response.data));
        }
        else {
            // delete musician from band
            axios.delete(url)
                .then((response) => console.log(response.data));
        }

        this.state.isMember = !(this.state.isMember);
        this.setButtonText();
    }

    setButtonText() {
        if (this.state.isMember) {
            this.state.joinButtonText = "Leave Band";
        }
        else {
            this.state.joinButtonText = "Join Band";
        }
    }

    render() {
        const params = new URLSearchParams(this.props.location.search);
        let curBandId = params.get("view");
        this.state.userId = Cookies.get("id");

        let bandsURL = getBackendURL() + "/bands";
        const {data : members} = axios.get(bandsURL + "/" + curBandId)
            .then((response) => this.setState(
                {
                    bandName: response.data.name,
                    bandId: response.data.id,
                    bandCapacity: response.data.capacity,
                    genres: response.data.genre,
                    members: response.data.members
                }));

        // TODO: determine if user is currently a member or not
        // this.state.isMember = false;

        if (curBandId && this.state.bandId) {
            return (
                <div>
                <Header/>
                <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                    <Navbar.Brand className="mx-auto">
                        Explore Bands!
                    </Navbar.Brand>
                </Navbar>
                    <h1>{this.state.bandName}</h1>
                    <h3>Capacity: {this.state.bandCapacity}</h3>

                    <Button onClick={() => {this.join_leave()}}>{this.state.joinButtonText}</Button>
                    <Button onClick={() => {this.goBack()}}>Go Back</Button>
                </div>
            );
        } else {

            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Explore Bands!
                        </Navbar.Brand>
                    </Navbar>
                    <h3>Coming Soon...</h3>
                    <Button onClick={() => {this.goBack()}}>Go Back</Button>
                </div>

            );
        }
    }

}

export default Band;