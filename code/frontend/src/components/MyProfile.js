import React from 'react';
import axios from "axios";
import {getFriendsDataFromApi} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Form from "react-bootstrap/Form";
import Header from "./Header";
import {Container, Navbar} from "react-bootstrap";
import BandApiService from '../utils/BandApiService';

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
            friendList: [],
            pending_incoming_requests: [],
            pending_outgoing_requests: []
        }

        this.renderCustomizeProfileHeader = this.renderCustomizeProfileHeader.bind(this);

        /*
        Promise.all([
            BandApiService.getIncomingFriendRequests(this.state.id)
    
          ]).then(responses => {
                console.log(responses[0].data)
                console.log(responses[1].data)
          }).catch(error => {
              console.log(error)
          })
          */
    }

    getDataFromApi() {
        BandApiService.getUserFriendList(this.state.id).then(res => {
            const { data } = res;
            this.setState({
                friendList: data
            })
        });
        BandApiService.getIncomingFriendRequests(this.state.id).then(res => {
            const { data } = res;
            this.setState({
                pending_incoming_requests: data
            })
        });

    }
    async componentDidMount() {
        const { fetchBandsForMusician } = this.props;


      // fetchBandsForMusician({id: this.state.id});
        getFriendsDataFromApi(this.state.id)
            .then(axios.spread((r1, r2, r3) => {
                this.setState({
                    friendList: r1.data,
                    pending_incoming_requests: r2.data,
                    pending_outgoing_requests: r3.data
                });
            })).catch((error) => console.log(error))
    }
        /*
        this.setState({
            friendList: responses[0].data,
            pending_incoming_requests: responses[1].data,
            pending_outgoing_requests: responses[2].data
        })
    }*/

    renderFriendListForMusician() {
        const listItems = this.state.friendList.map((friend) =>
        <li key={friend["id"]}>{friend["name"]}</li>
        );
        return (
            <ul>{listItems}</ul>
        );
    }

    renderIncomingRequestList() {
        console.log(this.state.pending_incoming_requests)
        const listItems = this.state.pending_incoming_requests.map((request) => 
        <li>{request.senderID}</li>
        );
        return (
            <ul>{listItems}</ul>
        );
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

        console.log(this.state)

        if (this.state.bands) {
            return (
                <div>
                    <Header/>
                    {this.renderCustomizeProfileHeader()}

                    <Tabs>
                        <TabList>
                            <Tab>My Profile</Tab>
                            <Tab>My Bands</Tab>
                            <Tab>My Friends</Tab> 
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
                            <Button onClick={() => { this.props.history.push('/editprofile');}}>Edit Profile</Button>
                        </TabPanel>

                        <TabPanel>
                            {bandsList}
                            <Button onClick={() => this.props.history.push('/createband')}>Create Band</Button>
                        </TabPanel>


                        <TabPanel>
                            <h3>My friends ({userInfo.friends?.length})</h3>
                            {this.renderFriendListForMusician()}
                            <h3>Friend requests ({this.state.pending_incoming_requests?.length})</h3>
                            {this.renderIncomingRequestList()}
                            <h3>Pending friend requests</h3>

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
