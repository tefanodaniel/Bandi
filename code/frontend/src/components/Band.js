import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Cookies from "js-cookie";
import Header from "./Header";
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
            buttonText: 'Join Band',

            memberNames: []
        }

        this.join_leave = this.join_leave.bind(this);
        this.setButtonText = this.setButtonText.bind(this);
    }

    join_leave() {

        const url = getBackendURL() + "/bands/" + this.state.bandId + "/" + this.state.userId;

        if (!this.state.isMember) {
            // put musician in the band
            axios.put(url)
                .then((response) => console.log(response.data));
        }
        else {
            // delete musician from band
            axios.delete(url)
                .then((response) => console.log(response.data));
        }

        this.setState({isMember: !this.state.isMember});
        this.setButtonText();
        window.location.reload();
    }

    setButtonText() {
        if (this.state.isMember) {
            this.state.buttonText = "Leave Band";
        }
        else {
            this.state.buttonText = "Join Band";
        }
    }

    getBandInfo() {
        const params = new URLSearchParams(this.props.location.search);
        let curBandId = params.get("view");
        this.state.userId = Cookies.get("id");

        let bandsURL = getBackendURL() + "/bands";
        axios.get(bandsURL + "/" + curBandId)
            .then((response) => this.setState(
                {
                    bandName: response.data.name,
                    bandId: response.data.id,
                    bandCapacity: response.data.capacity,
                    genres: response.data.genres,
                    members: response.data.members
                },
                function() {
                    this.setState({memberNames: []});

                    this.state.members.forEach(id => {
                        var userURL = getBackendURL() + "/musicians/" + id;
                        axios.get(userURL)
                            .then((response) =>
                                this.setState({memberNames:
                                        this.state.memberNames.concat(response.data.name)})
                            );
                    });
                }));
    }

    componentDidMount() {
        this.getBandInfo();
    }

    render() {

        // Determine if user is currently a member or not
        this.state.isMember = (this.state.members).indexOf(this.state.userId) > -1;
        this.setButtonText();

        if (this.state.userId) {
            return (
                <div>
                <Header/>
                <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                    <Navbar.Brand className="mx-auto">
                        Explore Bands!
                    </Navbar.Brand>
                </Navbar>
                    <h1>{this.state.bandName}</h1>
                    <h4>Genres: {this.state.genres.join(", ")}</h4>
                    <h4>Members: {this.state.memberNames.join(", ")}</h4>

                    <Button onClick={() => {this.join_leave()}}>{this.state.buttonText}</Button>
                    <Button onClick={() => {this.props.history.goBack()}}>Go Back</Button>
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
                    <Button onClick={() => {this.props.history.goBack()}}>Go Back</Button>
                </div>

            );
        }
    }

}

export default Band;
