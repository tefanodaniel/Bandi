import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import Button from "react-bootstrap/button";
import Cookies from "js-cookie";

import Header from "../Header/Header";
import SubHeader from "../Header/SubHeader";

import '../../styles/user_dashboard.css';
import ChatApi from "../../utils/ChatApiService";
import MusicianApi from "../../utils/MusicianApiService";

import { connect } from 'react-redux';
import { getIncomingFriendRequests, getUserFriends, takeActionOnFriendRequest } from '../../actions/friend_actions';
import EditUserInfo from './EditUserInfo';
import CreateBand from '../Bands/CreateBand';

class UserDashboard extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {
            id: Cookies.get('id'),
            editing: false, // tracks whether user is currently editing their profile
            creating: false // tracks whether user is in the process of creating a band
        }
    }

    async takeActionOnFriendRequest(request, action) {
        this.props.respondToFriendRequest(request.senderID, request.recipientID, action);
        if (action === "accept") {
            await ChatApi.addChatFriend(this.props.userInfo.id, request.senderID);
            alert("You accepted " + request.senderName + "'s friend request! You can now chat with each other.");
        } else if (action === "decline") {
            alert("You declined " + request.senderName + "'s friend request!");
        }
    }

    renderFriendListForMusician(friends) {
        if (friends && friends.length > 0) {
            const listItems = friends.map((friend) =>
            <li class="friends" key={friend["id"]}>{friend["name"]}</li>
            );
            return (
                <ul class="friend-container">{listItems}</ul>
            );
        } else {
            return <ul/>
        }
    }

    renderIncomingRequestList(incoming) {
        if (incoming && incoming.length > 0) {
            const listItems = incoming.map((request) =>
            <div>
                <li class="incoming">{request.senderName}<button class="bandi-button accept-button" onClick={() => this.takeActionOnFriendRequest(request, 'accept')}>Accept</button>
                <button class= "bandi-button decline-button" onClick={() => this.takeActionOnFriendRequest(request, 'decline')}>Decline</button></li>
            </div>
            );
            return (
                <ul class="friend-container">{listItems}</ul>
            );
        } else {
            return <ul/>
        }
    }

    renderOutgoingRequestList(outgoing) {
        if (outgoing && outgoing.length > 0) {
            const listItems = outgoing.map((request) =>
            <li class="pending">{request.recipientName}</li>
            );
            return (
                <ul class="friend-container">{listItems}</ul>
            );
        } else {
            return <ul/>
        }
    }

    renderBandList(bands) {
        if (bands && bands.length > 0) {
            var bandsList = bands.map((band) =>
            <div key={band.id} className="card">
                <div className="card-body">
                    <h5 className="card-title">{band.name}</h5>
                    <h6 className="card-subtitle">{band.genres.join(", ")}</h6>
                    <p className="card-text"></p>
                    <button onClick={() => { this.props.history.push('/band?view=' + band.id);}}>View More</button>
                </div>
            </div>);
            return bandsList;
        } else {
            return <div/>
        }
    }

    spotifyButton(isVisible) {
        var newSetting = !isVisible;
        MusicianApi.updateShowTopTracks(this.state.id, {"showtoptracks": newSetting});
        window.location.reload();
    }

    render() {

        // Get user information from our central redux store, rather than the limited state of this component
        const userInfo = this.props.userInfo;
        const friends = this.props.friends;
        const incoming = this.props.incoming_friend_requests;
        const outgoing = this.props.outgoing_friend_requests;
        const bands = this.props.userInfo.bands;

        let spotify_info_view;
        spotify_info_view = 
            <div class="spotify-info-panel">
                    <h4>Spotify Top Tracks</h4>
                    {userInfo?.topTracks ? <button id="set-top-tracks-visibility" class="bandi-button dashboard" onClick={() => {this.spotifyButton(userInfo?.showtoptracks);}}>{userInfo?.showtoptracks ? "Make private" : "Make publicly visible"}</button>
                            : " Loading..."}
                    <div id="visibility-status" class="bandi-box">
                        {userInfo?.topTracks ? (userInfo?.showtoptracks ? "Currently visible" : "Currently private") : ""}
                    </div>
                    <div class="bandi-box top-tracks-container">
                        <ol class="top-tracks-list">
                        {userInfo?.topTracks ? userInfo.topTracks.map((track, i) => <li>{track}</li>) : ""}
                        </ol>
                    </div>
            </div>;

        let profile_view;
        if (this.state.editing) {
            profile_view = 
                <div class="bandi-text-fields inner-panel">
                    <EditUserInfo/>
                    <button id="edit-profile" class="bandi-button dashboard"  onClick={() => {this.setState({editing: false})}}>Go back</button>
                </div>
        } else { // render user profile
            profile_view = 
                <div class="bandi-text-fields inner-panel">
                    <h2 class="name">{userInfo.name}</h2>
                    <h4 class="label" id="location"><span class="label-text">Location: </span>{userInfo.location === "NULL" ? "" : userInfo.location}</h4>
                    <h4 class="label"><span class="label-text">Experience: </span>{userInfo.experience === "NULL" ? "" : userInfo.experience}</h4>
                    <div>
                        <h4 class="label"><span class="label-text">Instruments: </span>{userInfo.instruments ? userInfo.instruments.join(", ") : ""}</h4>
                    </div>
                    <div>
                        <h4 class="label"><span class="label-text">Genres: </span>{userInfo.genres ? userInfo.genres.join(", ") : ""}</h4>
                    </div>
                    <div>
                        <h4 class="label"><span class="label-text">Links: </span>{userInfo.links ? userInfo.links.map((link, i) => <a href={link}>{link}</a>) : ""}</h4>
                    </div>
                    <div class="edit-profile-button-div">
                    <button id="edit-profile" class="bandi-button dashboard" onClick={() => {this.setState({editing: true})}}>Edit Profile</button>
                    </div>
                </div>;
        }

        let band_view;
        if (this.state.creating) {
            band_view = 
            <div>
                <button class="bandi-button dashboard" onClick={() => this.setState({creating: false})}>Go back</button>
                <CreateBand/>
            </div>
        } else {
            band_view = 
            <div>
                <button class="bandi-button dashboard" onClick={() => this.setState({creating: true})}>Create band</button>
                {this.renderBandList(bands)}
            </div>
        }


        
        if (userInfo) {
            return (
                <div class="outer-dashboard">
                    <Header/>
                    <div>
                        <div class="tabs-container">
                            <Tabs class="child">
                                <TabList>
                                    <Tab>My Profile</Tab>
                                    <Tab>My Bands</Tab>
                                    <Tab>My Friends</Tab>
                                </TabList>

                                <TabPanel className="profile-panel">
                                {profile_view}
                                {spotify_info_view}
                                </TabPanel>

                                <TabPanel>
                                {band_view}
                                </TabPanel>


                                <TabPanel>
                                    <div class="friend-panel">
                                        <h3 id="friend-label-1">My friends ({friends?.length})</h3>
                                        {this.renderFriendListForMusician(friends)}
                                        <h3 id="friend-label-2">Friend requests ({incoming?.length})</h3>
                                        {this.renderIncomingRequestList(incoming)}
                                        <h3 id="friend-label-3">Pending friend requests ({outgoing?.length})</h3>
                                        {this.renderOutgoingRequestList(outgoing)}
                                    </div>

                                </TabPanel>

                            </Tabs>
                        </div>
                    </div>
                </div>
            );
        } else {

            return (
                <div>
                    <Header/>
                    <SubHeader text="Customize your profile"/>
                    <h3>Coming Soon...</h3>
                </div>
            );
        }
    }

}

/* Redux stuff
 *
 * This is how we:
 *      1) get access to the global store (state shared over the entire app)
 *      2) get access to the dispatch objects (functions) we can use to modify the global store
 */


function mapStateToProps(state) {
  return {
    // changed so that we only keep the parts of the store that are relevant to UserDashboard
    // can still access whole store using 'state'
    userInfo: state.user_reducer,
    friends: state.friend_reducer.friend_info,
    incoming_friend_requests: state.friend_reducer.incoming_friend_requests,
    outgoing_friend_requests: state.friend_reducer.outgoing_friend_requests
  };
} // end mapStateToProps

function mapDispatchToProps(dispatch) {
    return {
        fetchFriends: (id) => dispatch(getUserFriends(id)),
        fetchIncoming: (id) => dispatch(getIncomingFriendRequests(id)),
        respondToFriendRequest: (senderID, recipientID, action) => {
            dispatch(takeActionOnFriendRequest(senderID, recipientID, action))
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(UserDashboard);
