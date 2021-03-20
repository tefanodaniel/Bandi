import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Cookies from "js-cookie";

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
            joinButtonText: 'Join Band',

            memberNames: []
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

    getNames() {
        this.setState({memberNames: []});
        this.state.members.forEach(id => {
            var userURL = getBackendURL() + "/musicians/" + id;
            axios.get(userURL)
                .then((response) =>
                    this.setState({memberNames: this.state.memberNames.concat(response.data.name)}));
        });
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
                }));

        //this.getNames();
    }

    componentDidMount() {
        this.getBandInfo();

        //this.setState({memberNames: []});
        this.state.members.forEach(id => {
            var userURL = getBackendURL() + "/musicians/" + id;
            axios.get(userURL)
                .then((response) =>
                    this.setState({memberNames: this.state.memberNames.concat(response.data.name)}));
        });
    }

    render() {

        // Determine if user is currently a member or not
        this.state.isMember = (this.state.members).indexOf(this.state.userId) > -1;
        this.setButtonText();

        if (this.state.bandId) {
            return (
                <div>
                    <h1>{this.state.bandName}</h1>
                    <h4>Genres: {this.state.genres.join(", ")}</h4>
                    <h4>Members: {this.state.memberNames.join(", ")}</h4>

                    <Button onClick={() => {this.join_leave()}}>{this.state.joinButtonText}</Button>
                    <Button onClick={() => {this.goBack()}}>Go Back</Button>
                </div>
            );
        } else {

            return (
                <div>
                    <h1>View Band</h1>
                    <h3>Loading...</h3>
                    <Button onClick={() => {this.goBack()}}>Go Back</Button>
                </div>

            );
        }
    }

}

export default Band;