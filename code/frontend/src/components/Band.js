import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

class Band extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

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

        if (curBandId && this.state.bandId) {
            return (
                <div>
                    <h1>{this.state.bandName}</h1>
                    <h3>Capacity: {this.state.bandCapacity}</h3>

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