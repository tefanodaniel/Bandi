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
            bandSize : 0,
            bandCapacity : 0,
            genres: [],
            members: []
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
                    bandSize: response.data.size,
                    bandCapacity: response.data.capacity,
                    genres: response.data.genre,
                    members: response.data.members
                }));

        if (curBandId && this.state.bandId) {
            return (
                <div>
                    <h1>{this.state.bandName}</h1>
                    <h3>{this.state.bandSize} / {this.state.bandCapacity} spots filled</h3>
                    <Button onClick={() => {this.props.history.push('/myprofile')}}>Go Back</Button>
                </div>
            );
        } else {

            return (
                <div>
                    <h1>View Band</h1>
                    <h3>Loading...</h3>
                </div>

            );
        }
    }

}

export default Band;