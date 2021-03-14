import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";

class Profile extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {
            bands: []
        }
    }

    // get request functions here

    render() {
        var bandsURL = getBackendURL() + "/bands";
        var frontendURL = getFrontendURL();
        axios.get(bandsURL)
            .then((response) => this.setState({bands: response.data}));

        // Get list of band views
        var bandsList = this.state.bands.map((band) =>
            <div className="card">
                <div className="card-body">
                    <h5 className="card-title">{band.name}</h5>
                    <h6 className="card-subtitle">{band.genre}</h6>
                    <p className="card-text"></p>
                    <a href={frontendURL + "/profiles/" + band.name}>View Profile</a>
                </div>
            </div>
        );

        if (this.state.bands) {
            return (
                <div>
                    <h3>Profile</h3>
                    <button onClick={() => {
                        this.props.history.push('/');
                    }}>Back to Discover
                    </button>
                    {bandsList}
                </div>
            );
        } else {
            return (<h1>Loading...</h1>);
        }
    }

}
export default Profile;