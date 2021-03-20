import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Form from "react-bootstrap/Form";
import Header from "./Header/Header";
import {Container, Navbar} from "react-bootstrap";

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
                    <h6 className="card-subtitle">{band.genres.join(", ")}</h6>
                    <p className="card-text"></p>
                    <Button onClick={() => { this.props.history.push('/band?view=' + band.id);}}>View More</Button>
                </div>
            </div>
        );

        if (this.state.bands && this.state.bands.length > 0) {
            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Find bands you love here!
                        </Navbar.Brand>
                    </Navbar>

                    {bandsList}
                </div>
            );
        } else {

            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Find bands you love here!
                        </Navbar.Brand>
                    </Navbar>


                    <h3> Coming Soon...</h3>
                </div>

            );
        }
    }

}
export default BandView;