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
            name: '',
            capacity: 0,
            genres: [],
            members: []
        }

        this.submit_form = this.submit_form.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    submit_form() {
        const axios = require('axios')
        const url = getBackendURL() + "/bands/";

        axios
            .post(url, {
                id : "foo",
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

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    render() {
        return (
            <div>
            <h1>Create Band</h1>


                <Form>
                    <Form.Group controlId="profileForm.name">
                        <Form.Label>Name of Band</Form.Label>
                        <Form.Control name="" type="input"
                                      placeholder="ex. The Beatles" value={this.state.name} onChange={this.handleChange} />
                    </Form.Group>
                </Form>



            <Button onClick={this.submit_form}>Create</Button>

            <Button onClick={() => {this.props.history.push('/myprofile')}}>Go Back</Button>
            </div>
        )
    }
}

export default CreateBand;
