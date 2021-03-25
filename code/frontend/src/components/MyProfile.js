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
            genres: [],
            links: [],
            friends: [],
            pending_outgoing_requests: [],
        }

        // get user's id
        console.log("am I over here?")
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
                experience: response.data.experience,
                instruments: response.data.instruments,
                genres: response.data.genres,
                links: response.data.profileLinks
                //friends: response.data.friends,
                //pending_requests: response.data.pending_outgoing_requests}));
                }));
        
    }

    getFriendsAndUrls() {
        let friends = new Map();
        console.log(this.state);
        this.state.friends.forEach(friendID => {
            let friendURL = getBackendURL() + "/musicians/" + friendID;
            axios.get(friendURL)
                .then(response => friends.set(response.data.name, friendURL));
        });
        console.log(friends);
        return friends;
    }



    render() {

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



        const FriendsList = () => {
            let friends = this.getFriendsAndUrls();
            if (friends.size > 0) {
                const friendItems = friends.map((name, url) => {
                        <li> <a href={url}>{name}</a> </li>
                });
                return (
                  <ul>{friendItems}</ul>
                );
            } else {
                return (<p>No friends to display :(</p>);
            }
        }

        if (this.state.bands) {
            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Customize your Profile
                        </Navbar.Brand>
                    </Navbar>
                    <Tabs>
                        <TabList>
                            <Tab>My Profile</Tab>
                            <Tab>My Bands</Tab>
                            <Tab>My Friends</Tab> 
                        </TabList>

                        <TabPanel>
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
                                <h4>Links: {this.state.links.map((link, i) => <a href={link}>{link}</a>)}</h4>
                            </div>
                            <Button onClick={() => {; this.props.history.push('/editprofile');}}>Edit Profile</Button>
                        </TabPanel>

                        <TabPanel>
                            {bandsList}
                            <Button onClick={() => {this.props.history.push('/createband')}}>Create Band</Button>
                        </TabPanel>


                        <TabPanel>
                            <FriendsList/>
                        </TabPanel>

                    </Tabs>
                </div>
            );
        } else {

            return (
                <div>
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Customize your Profile Page
                        </Navbar.Brand>
                    </Navbar>
                    <h3>Coming Soon...</h3>
                </div>

            );
        }
    }

}
export default MyProfile;