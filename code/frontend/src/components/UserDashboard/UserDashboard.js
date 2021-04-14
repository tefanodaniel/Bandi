import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/Button";
import Cookies from "js-cookie";
import Header from "../Header/Header";
import {Container, Navbar} from "react-bootstrap";
import BandApiService from '../../utils/BandApiService';
import FriendApiService from '../../utils/FriendApiService';
import { bandi_styles } from "../../styles/bandi_styles";


import { connect } from 'react-redux';
import { fetchBandsForMusician } from '../../actions/band_actions';
import { getIncomingFriendRequests, getUserFriends } from '../../actions/friend_actions';

class UserDashboard extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {
            id: Cookies.get('id'),
            bands: []
        }

        const id = Cookies.get("id");
        this.renderCustomizeProfileHeader = this.renderCustomizeProfileHeader.bind(this);


    }

    componentDidMount() {
        //const { fetchBandsForMusician } = this.props;

        // fetchBandsForMusician({id: this.state.id});
        // this.updateFriendPanel();

    }
    
    async takeActionOnFriendRequest(request, action) {
        const response = await FriendApiService.respondToFriendRequest(request.senderID, request.recipientID, action);
        this.props.fetchFriends(this.props.userInfo.id)
        if (action === "accept") {
            alert("You accepted " + request.senderName + "'s friend request!");
        } else if (action === "decline") {
            alert("You declined " + request.senderName + "'s friend request!");
        }
    }
    
    renderFriendListForMusician(friends) {
        if (friends && friends.length > 0) {            
            const listItems = friends.map((friend) =>
            <li key={friend["id"]}>{friend["name"]}</li>
            );
            return (
                <ul>{listItems}</ul>
            );
        } else {
            return <ul/>
        }
    }

    renderIncomingRequestList(incoming) {
        if (incoming && incoming.length > 0) {
            const listItems = incoming.map((request) =>
            <div>
                <li>{request.senderName}<Button onClick={() => this.takeActionOnFriendRequest(request, 'accept')}>Accept</Button>
                <Button onClick={() => this.takeActionOnFriendRequest(request, 'decline')}>Decline</Button></li>
            </div>
            );
            return (
                <ul>{listItems}</ul>
            );
        } else {
            return <ul/>
        }
    }

    renderOutgoingRequestList(outgoing) {
        if (outgoing && outgoing.length > 0) {
            const listItems = outgoing.map((request) =>
            <li>{request.recipientName}</li>
            );
            return (
                <ul>{listItems}</ul>
            );
        } else {
            return <ul/>
        }
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
        const userInfo = this.props.userInfo;
        const friends = this.props.friends;
        const incoming = this.props.incoming_friend_requests;
        const outgoing = this.props.outgoing_friend_requests;

        if (this.state.bands) {
            return (
                <div style={bandi_styles.discover_background}>
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
                            <h3>My friends ({friends?.length})</h3>
                            {this.renderFriendListForMusician(friends)}
                            <h3>Friend requests ({incoming?.length})</h3>
                            {this.renderIncomingRequestList(incoming)}
                            <h3>Pending friend requests ({outgoing?.length})</h3>
                            {this.renderOutgoingRequestList(outgoing)}

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
    //store: state,
    userInfo: state.user_reducer,
    friends: state.friend_reducer.friend_info,
    incoming_friend_requests: state.friend_reducer.incoming_friend_requests,
    outgoing_friend_requests: state.friend_reducer.outgoing_friend_requests
  };
} // end mapStateToProps

function mapDispatchToProps(dispatch) {
    return {
        fetchFriends: (id) => {
            console.log("fetching friends after friend request action!")
            dispatch(getUserFriends(id))
        },
        fetchIncoming: () => dispatch(getIncomingFriendRequests("1")),
        fetchBands: () => dispatch(fetchBandsForMusician("1"))
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(UserDashboard);
