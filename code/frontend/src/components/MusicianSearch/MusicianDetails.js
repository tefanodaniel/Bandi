import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../../utils/api";
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import {TabPanel} from "react-tabs";
import Header from "../Header/Header";
import {Container, Navbar} from "react-bootstrap";
import FriendApiService from '../../utils/FriendApiService';
import MusicianApi from "../../utils/MusicianApiService";

import '../../styles/musiciandetails.css';

class MusicianDetails extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

            my_id : '',

            view_id : '',
            name: 'Loading...',
            location: '',
            experience: '',
            instruments: [],
            genres: [],
            links: [],
            friends: [],
            toptracks: [],
            showtoptracks: true
        }

        const params = new URLSearchParams(this.props.location.search);
        this.state.view_id = params.get("view");
        this.state.my_id = Cookies.get("id");
    }

    componentDidMount() {
        var view_id = this.state.view_id;
        MusicianApi.get(view_id)
            .then((response) => {
                this.setState({name: response.data.name,
                        location: response.data.location,
                        experience: response.data.experience,
                        instruments: response.data.instruments,
                        genres: response.data.genres,
                        links: response.data.profileLinks,
                        friends: response.data.friends,
                        toptracks: response.data.topTracks,
                        showtoptracks: response.data.showtoptracks
                    }); console.log(response.data)
            });

        console.log(this.state.view_id);
        console.log(this.state.name);
    }

    tracks() {
        if (this.state.showtoptracks) {
            return(<div>{this.state.toptracks.map((track, i) => <li>{track}</li>)}</div>)
        }
        else {
            return(<div>(hidden)</div>)
        }

    }

    render() {

            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Learn more about fellow Musicians!
                        </Navbar.Brand>
                    </Navbar>

                    <div class="musicianInfo">

                    <h2>Name: {this.state.name}</h2>
                    <h4>Location: {this.state.location}</h4>
                    <h4>Experience: {this.state.experience}</h4>
                    <div>
                        <h4>Instruments: {this.state.instruments.join(", ")}</h4>
                    </div>
                    <div>
                        <h4>Genres: {this.state.genres.join(", ")}</h4>
                    </div>
                    <div>
                        <h4>Links: </h4>{this.state.links.map((link, i) => <li><a href={link}>{link}</a></li>)}
                    </div>

                    <div>
                        <h4>Top Spotify Tracks: </h4>{this.tracks()}
                    </div>

                    </div>

                </div>
            );

    }

}

export default MusicianDetails;
