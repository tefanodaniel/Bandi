import React from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup'
import Cookies from "js-cookie";
import {getBackendURL} from "../../utils/api";
import Header from "../Header/Header";
import {Container, Navbar} from "react-bootstrap";

import BandApi from "../../utils/BandApiService";
import ChatApi from "../../utils/ChatApiService";

class CreateBand extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            name: '',
            capacity: 0,
            genres: [],
            members: [],
            errorText: ''
        }

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleGenreSelection = this.handleGenreSelection.bind(this);
    }

    handleSubmit() {
        // post band

        if (this.state.name == '' || this.state.genres.length == 0) {
            this.setState({errorText : 'Error: Please fill out all fields'})
            return;
        }

        BandApi.create({
                id: "foo",
                name: this.state.name,
                members: this.state.members,
                genres: this.state.genres
            })
            .then(res => {
                console.log(`statusCode: ${res.statusCode}`)
                console.log(res)

                // create group chat for the newly created band
                ChatApi.createBandGroupChat(res.data.id, res.data.name, "private")
                  .then(() => {
                    console.log("Successfuly created group chat for ", res.data.name)
                    ChatApi.addMembersToGroupChat(res.data.id, {admins: [Cookies.get("id")]})
                      .then(() => {
                        console.log("Added current user as admin")
                      })
                      .catch(error => {
                        console.log(error)
                      })
                  })
                  .catch(error => {
                    console.log(error)
                  })
            })
            .catch(error => {
                console.error(error)
            })
        this.props.history.push('/myprofile')
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
                    <Header/>
                    <Navbar expand="lg" variant="light" bg="light" className="mx-auto">
                        <Navbar.Brand className="mx-auto">
                            Create a new Band!
                        </Navbar.Brand>
                    </Navbar>

                    <h3>{this.state.errorText}</h3>

                    <Form onSubmit={() => this.handleSubmit()}>
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

                        <Button type="submit">Create</Button>

                    </Form>


                    <Button onClick={() => {this.props.history.push('/myprofile')}}>Go Back</Button>

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
