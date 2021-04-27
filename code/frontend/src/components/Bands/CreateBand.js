import React from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup'
import Cookies from "js-cookie";
import {getBackendURL} from "../../utils/api";
import Header from "../Header/Header";
import {Container, Navbar} from "react-bootstrap";

import BandApi from "../../utils/BandApiService";

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
        this.handleGenreSelection = this.handleGenreSelection.bind(this);
    }

    submit_form() {
        // post band
        BandApi.create({
                id : "foo",
                name: this.state.name,
                members: this.state.members,
                genres: this.state.genres
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

    handleGenreSelection(event) {
        const target = event.target;
        const name = target.name;
        let newGenreList = this.state.genres.slice();
        if (target.checked) {
            newGenreList.push(name);
        } else {
            const index = newGenreList.indexOf(name);
            if (index > -1) { newGenreList.splice(index, 1); }
        }
        this.setState({
            genres: newGenreList
        });
    }


    componentDidMount() {
        this.setState({members: [Cookies.get("id")]});
    }

    render() {

        if (this.state.members) {
            return (
                <div>
                    <Form onSubmit={"nothing"}>
                        <Form.Group controlId="profileForm.name">
                            <Form.Label>Band Name:</Form.Label>
                            <Form.Control name="name" type="input"
                                          placeholder="ex. The Beatles" value={this.state.name}
                                          onChange={this.handleChange}/>
                        </Form.Group>

                        <FormGroup controlId="profileForm.genres">
                            <Form.Label>Genres:</Form.Label>
                            <Form.Check inline name="rock" label="Rock" type="checkbox" onChange={this.handleGenreSelection}/>
                            <Form.Check inline name="blues" label="Blues" type="checkbox" onChange={this.handleGenreSelection}/>
                            <Form.Check inline name="jazz" label="Jazz" type="checkbox" onChange={this.handleGenreSelection}/>
                            <Form.Check inline name="classical" label="Classical" type="checkbox" onChange={this.handleGenreSelection}/>
                        </FormGroup>

                    </Form>

                    <Button onClick={this.submit_form}>Create</Button>
                </div>
            )
        }
        else {
            return (
                <div>
                    <h1>Loading...</h1>
                </div>
            )
        }
    }
}

export default CreateBand;
