import React from 'react';
import axios from "axios";
import {getBackendURL, getFrontendURL} from "../utils/api";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

class Band extends React.Component {
    constructor(props) {
        super(props)

        // Define the state for this component
        this.state = {

        }
    }

    render() {

        if (true) {
            return (
                <div>
                    <h1>Band</h1>

                </div>
            );
        } else {

            return (
                <div>
                    <h1>Band</h1>
                    <h3>Loading...</h3>
                </div>

            );
        }
    }

}

export default Band;