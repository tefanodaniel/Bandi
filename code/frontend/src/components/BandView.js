import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Header from "./Header/Header";
import SubHeader from "./Header/SubHeader";
import { bandi_styles } from "../styles/bandi_styles"
import {Card} from "react-bootstrap";

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
            <Card style={bandi_styles.band_card} className="rounded border-0">
            <Card.Body>
                <Card.Title>{band.name}</Card.Title>
                <Card.Text className="small font-italic" style={{textColor:"white"}}>Genres: {band.genres.join(", ")}</Card.Text>
                <Button className="small font-italic" onClick={() => { this.props.history.push('/band?view=' + band.id);}}>View More</Button>
            </Card.Body>
            </Card>
        );

        if (this.state.bands && this.state.bands.length > 0) {
            return (
                <div className="bg-transparent" style={bandi_styles.discover_background}>
                    <Header/>
                    <SubHeader text={"Find bands you love here!"}/>
                    {bandsList}
                </div>
            );
        } else {

            return (
                <div className="bg-transparent">
                    <Header/>
                    <SubHeader text={"Find bands you love here!"}/>
                    <h3> Coming Soon...</h3>
                </div>

            );
        }
    }

}
export default BandView;
