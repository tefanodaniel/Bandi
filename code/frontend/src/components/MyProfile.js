import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Form from "react-bootstrap/Form";
import Header from "./Header";
import {Container, Navbar} from "react-bootstrap";

import { connect } from 'react-redux';
import { fetchBandsForMusician } from '../actions/band_actions';



class MyProfile extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

            id: Cookies.get('id'),

            bands: [],

            name: 'Loading...',
            location: '',
            experience: '',
            instruments: [],
            genres: [],
            links: []
        }

        this.renderCustomizeProfileHeader = this.renderCustomizeProfileHeader.bind(this);
    }

    componentDidMount() {
      const { fetchBandsForMusician } = this.props;
      fetchBandsForMusician({id: this.state.id});
    }

    renderCustomizeProfileHeader() {
      return (
        <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
            <Navbar.Brand className="mx-auto">
                Customize your Profile
            </Navbar.Brand>
        </Navbar>
      );
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

        // Get user information from our central redux store, rather than the limited state of this component
        const userInfo = this.props.store.user_reducer;
        console.log('here here', userInfo);

        if (this.state.bands) {
            return (
                <div>
                    <Header/>
                    {this.renderCustomizeProfileHeader()}

                    <Tabs>
                        <TabList>
                            <Tab>My Profile</Tab>
                            <Tab>My Bands</Tab>
                        </TabList>

                        <TabPanel>
                            <h2>Name: {userInfo.name}</h2>
                            <h4>Location: {userInfo.location === "NULL" ? "" : userInfo.location}</h4>
                            <h4>Experience: {userInfo.experience === "NULL" ? "" : userInfo.experience}</h4>
                            <div>
                                <h4>Instruments: {userInfo.instruments ? userInfo.instruments.join(", ") : ""}</h4>
                            </div>
                            <div>
                                <h4>Genres: {userInfo.genres ? userInfo.genres.join(", ") : ""}</h4>
                            </div>
                            <div>
                                <h4>Links: {userInfo.links ? userInfo.links.map((link, i) => <a href={link}>{link}</a>) : ""}</h4>
                            </div>
                            <Button onClick={() => {; this.props.history.push('/editprofile');}}>Edit Profile</Button>
                        </TabPanel>

                        <TabPanel>
                            {bandsList}
                            <Button onClick={() => this.props.history.push('/createband')}>Create Band</Button>
                        </TabPanel>

                    </Tabs>
                </div>
            );
        } else {

            return (
                <div>
                    <Header/>
                    {this.renderCustomizeProfileHeader()}
                    <h3>Coming Soon...</h3>
                </div>

            );
        }
    }

}

function mapStateToProps(state) {
  return {
    store: state
  };
} // end mapStateToProps
export default connect(mapStateToProps, {fetchBandsForMusician})(MyProfile);
