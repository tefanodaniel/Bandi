import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Form from "react-bootstrap/Form";

class BandView extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {
            id: '',
            bands: []
        }
    }

    render() {
        // get user's id
        this.state.id = Cookies.get('id');

        var bandsURL = getBackendURL() + "/bands";

        // get bands
        axios.get(bandsURL)
            .then((response) => this.setState({bands: response.data}));

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

        if (this.state.bands && this.state.bands.length > 0) {
            return (
                <div>
                    <header>
                        <h1>Browse Bands</h1>

                        <Button onClick={() => {
                            this.props.history.push('/');
                        }}>Back to Discover
                        </Button>

                    </header>

                    {bandsList}
                </div>
            );
        } else {

            return (
                <div>
                    <h1>Browse Bands</h1>

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
export default BandView;