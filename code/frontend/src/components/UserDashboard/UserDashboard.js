import React from 'react';
import axios from "axios";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Header from "../Header/Header";
import {Container, Navbar} from "react-bootstrap";
import BandApiService from '../../utils/BandApiService';
import FriendApiService from '../../utils/FriendApiService';
import {getFriendsDataFromApi} from "../../utils/api";

import { connect } from 'react-redux';
import { fetchBandsForMusician } from '../../actions/band_actions';

class UserDashboard extends React.Component {
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

    }

    componentDidMount() {
        const { fetchBandsForMusician } = this.props;


        fetchBandsForMusician({id: this.state.id});

        getFriendsDataFromApi(this.state.id).then((res) => {
          console.log(res);
          this.setState({
            friendList: res.friends,
            pending_incoming_requests: res.incoming,
            pending_outgoing_requests: res.outgoing
          });
        });
        /*
        getFriendsDataFromApi(this.state.id)
            .then(axios.spread((r1, r2, r3) => {
                this.setState({
                    friendList: r1.data,
                    pending_incoming_requests: r2.data,
                    pending_outgoing_requests: r3.data
                });
            })).catch((error) => console.log(error)) */
    }

    renderFriendListForMusician() {
        const listItems = this.state.friendList.map((friend) =>
        <li key={friend["id"]}>{friend["name"]}</li>
        );
        return (
            <ul>{listItems}</ul>
        );
    }

    takeActionOnFriendRequest(request, action) {
        FriendApiService.respondToFriendRequest(request.recipientID, request.senderID, action);
        window.location.reload();
    }


    renderIncomingRequestList() {
        console.log(this.state.pending_incoming_requests)
        const listItems = this.state.pending_incoming_requests.map((request) =>
        <div>
            <li>{request.senderName}<Button onClick={() => this.takeActionOnFriendRequest(request, 'accept')}>Accept</Button>
            <Button onClick={() => this.takeActionOnFriendRequest(request, 'decline')}>Decline</Button></li>
        </div>
        );
        return (
            <ul>{listItems}</ul>
        );
    }

    renderOutgoingRequestList() {
        const listItems = this.state.pending_outgoing_requests.map((request) =>
        <li>{request.recipientName}</li>
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
                            <Button onClick={() => { this.props.history.push('/edit-user-info');}}>Edit Profile</Button>
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
                            <h3>Pending friend requests ({this.state.pending_outgoing_requests?.length})</h3>
                            {this.renderOutgoingRequestList()}

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
export default connect(mapStateToProps, {fetchBandsForMusician})(UserDashboard);
