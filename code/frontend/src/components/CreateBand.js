import React from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup'
import Cookies from "js-cookie";
import {getBackendURL} from "../utils/api";

class CreateBand extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id : '',
            name: '',
            genres: [],
            members: []
        }
    }

    submit_form() {
        const axios = require('axios')
        const url = getBackendURL() + "/bands";

        axios
            .post(url + "/" + this.state.id, {
                id : this.state.id,
                name: this.state.name
            })
            .then(res => {
                console.log(`statusCode: ${res.statusCode}`)
                console.log(res)
            })
            .catch(error => {
                console.error(error)
            })
    }

    render() {
        return (
            <div>
            <h1>Create Band</h1>
            <Button onClick={() => {this.props.history.push('/myprofile')}}>Go Back</Button>
            </div>
        )
    }
}

export default CreateBand;
