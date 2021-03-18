import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";

class Profile extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

            id: '',

            bands: [],

            name: '',
            location: '',
            genre: '',
            instruments: []
        }
    }



    render() {
        // get user's id
        let url = getBackendURL() + "/id";
        axios.get(url)
            .then((response) => this.setState({id: response.data.id}));

        var bandsURL = getBackendURL() + "/bands";
        var userURL = getBackendURL() + "/musicians/" + this.state.id;

        // get bands
        axios.get(bandsURL)
            .then((response) => this.setState({bands: response.data}));

        // get the user info
        axios.get(userURL)
            .then((response) => this.setState(
                {name: response.data.name, location: response.data.location,
                genre: response.data.genre}));

        // Get list of band views
        var bandsList = this.state.bands.map((band) =>
            <div className="card">
                <div className="card-body">
                    <h5 className="card-title">{band.name}</h5>
                    <h6 className="card-subtitle">{band.genre}</h6>
                    <p className="card-text"></p>
                    <Button onClick={() => { this.props.history.push('/band');}}>View More</Button>
                </div>
            </div>
        );

        if (this.state.bands && this.state.bands.length > 0) {
            return (
                <div>
                    <h1>My Profile Page</h1>
                    <button onClick={() => {
                        this.props.history.push('/');
                    }}>Back to Discover
                    </button>
                    <Tabs>
                        <TabList>
                            <Tab>My Profile</Tab>
                            <Tab>My Bands</Tab>
                        </TabList>

                        <TabPanel>
                            <h2>Name: {this.state.name}</h2>
                            <h4>Location: {this.state.location}</h4>
                            <h4>Genre: {this.state.genre}</h4>
                            <Button onClick={() => { this.props.history.push('/editprofile');}}>Edit Profile</Button>
                        </TabPanel>

                        <TabPanel>
                            {bandsList}
                        </TabPanel>
                    </Tabs>
                </div>
            );
        } else {

            return (
                <div>
                    <h1>Profile</h1>
                    <h3>Loading...</h3>
                </div>

            );
        }
    }

}
export default Profile;