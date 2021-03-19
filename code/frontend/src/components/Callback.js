import React from 'react';
import { Redirect } from 'react-router-dom';
import {findCookie, getBackendURL, getFrontendURL, getURL, logout} from "../utils/api";
import axios from "axios";

class Callback extends React.Component {
    constructor(props) {
        super(props)

        this.state = {

        }
    }


    render() {
        /*
        let url = getBackendURL() + "/id";
        axios.get(url)
            .then((response) => this.setState({id: response.data.id}));
*/
        return (<h1>callback</h1>);


        /*
        if (!this.state.id) {
            return (<h1>Loading...</h1>);
        }
        else if (this.state.id == "LOGGED_OUT") {
            return (<Redirect to="/signin"/>);
        }
        else { // some valid user id is present
            this.props.history.push('/');
           return null;
        }*/

    }
}

export default Callback;
