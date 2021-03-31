import React from 'react';
import {bandi_styles} from "../styles/bandi_styles";
import Header from "./Header";
import SubHeader from "./SubHeader";
import Cookies from "js-cookie";

import { connect } from 'react-redux';
import { fetchSDEvents } from '../actions/sd_event_actions';
import SDEventApi from "../utils/SDEventApiService";

class SpeedDate extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            id: Cookies.get('id'),
            sdEvents: []
        }
    }

    componentDidMount() {
        SDEventApi.getAll()
            .then((response) =>
            this.setState({sdEvents : response.data}));;
    }

    render() {

        console.log(this.state.sdEvents);

        return (
            <div className="bg-transparent" style={bandi_styles.discover_background}>
            <Header/>
            <SubHeader text={"Register for speed-dating events here!"}/>

            <h1>Speed-Dating Events:</h1>

            </div>
        )
    }

}

function mapStateToProps(state) {
    return {state};
}

export default connect(mapStateToProps)(SpeedDate);