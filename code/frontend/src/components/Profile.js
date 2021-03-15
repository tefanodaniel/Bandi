import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";

class Profile extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {
            bands: []
        }
    }

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
                    <Button onClick={() => { this.props.history.push('/band');}}>View More</Button>
                </div>
            </div>
        );

        if (this.state.bands && this.state.bands.length > 0) {
            return (
                <div>
                    <h1>Profile</h1>
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