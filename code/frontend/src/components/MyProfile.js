import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Form from "react-bootstrap/Form";

class MyProfile extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

            id: '',

            bands: [],

            name: 'Loading...',
            location: '',
            experience: '',
            instruments: [],
            genres: []
        }
    }



    render() {
        // get user's id
        this.state.id = Cookies.get('id');

        var bandsURL = getBackendURL() + "/bands" + "?musicianId=" + this.state.id;
        var userURL = getBackendURL() + "/musicians/" + this.state.id;

        // get bands
        axios.get(bandsURL)
            .then((response) => this.setState({bands: response.data}));

        // get the signed-in user's info
        axios.get(userURL)
            .then((response) => this.setState(
                {name: response.data.name, location: response.data.location,
                experience: response.data.experience}));

        // Generate a list of band views
        var bandsList = this.state.bands.map((band) =>
            <div className="card">
                <div className="card-body">
                    <h5 className="card-title">{band.name}</h5>
                    <h6 className="card-subtitle"></h6>
                    <p className="card-text"></p>
                    <Button onClick={() => { this.props.history.push('/band?view=' + band.id);}}>View More</Button>
                </div>
            </div>
        );

        if (this.state.bands) {
            return (
                <div>
                    <header>
                        <h1>My Profile Page</h1>

                        <Button onClick={() => {
                            this.props.history.push('/');
                        }}>Back to Discover
                        </Button>

                    </header>

                    <Tabs>
                        <TabList>
                            <Tab>My Profile</Tab>
                            <Tab>My Bands</Tab>
                        </TabList>

                        <TabPanel>
                            <h2>Name: {this.state.name}</h2>
                            <h4>Location: {this.state.location}</h4>
                            <h4>Experience: {this.state.experience}</h4>
                            <Button onClick={() => {; this.props.history.push('/editprofile');}}>Edit Profile</Button>
                        </TabPanel>

                        <TabPanel>
                            {bandsList}
                            <Button onClick={() => {this.props.history.push('/createband')}}>Create Band</Button>
                        </TabPanel>

                    </Tabs>
                </div>
            );
        } else {

            return (
                <div>
                    <h1>My Profile Page</h1>
                    <Button onClick={() => {
                        this.props.history.push('/');
                    }}>Back to Discover
                    </Button>
                    <h3>Loading...</h3>
                </div>

            );
        }
    }

}
export default MyProfile;