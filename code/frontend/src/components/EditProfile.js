import React from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup'
import Cookies from "js-cookie";
import {getBackendURL} from "../utils/api";

class EditProfile extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        id : '',

        name: '',
        location: '',
        experience: '',
        instruments: [],
        genres: []
      }

      this.handleSubmit = this.handleSubmit.bind(this);
      this.handleChange = this.handleChange.bind(this);
      this.handleInstrumentSelection = this.handleInstrumentSelection.bind(this);
    }

    handleChange(event) {
      const target = event.target;
      const value = target.value;
      const name = target.name;
      this.setState({
        [name]: value
      });
    }

    handleInstrumentSelection(event) {
      const target = event.target;
      const name = target.name;
      let newInstrumentList = this.state.instruments.slice();
      if (target.checked) {
        newInstrumentList.push(name);
      } else {
        const index = newInstrumentList.indexOf(name);
        if (index > -1) { newInstrumentList.splice(index, 1); }
      }
      this.setState({
        instruments: newInstrumentList
      });

    }

    handleSubmit(event) {
      const axios = require('axios')
      const url = getBackendURL() + "/musicians";

      axios
        .put(url, {
          id : this.state.id,
          name: this.state.name,
          location: this.state.location
        })
        .then(res => {
          console.log(`statusCode: ${res.statusCode}`)
          console.log(res)
        })
        .catch(error => {
          console.error(error)
        })
        event.preventDefault();
    }

    submit_form() {
      const axios = require('axios')
      const url = getBackendURL() + "/musicians" + "/" + Cookies.get("id");

      axios
          .put(url, {
              id: Cookies.get("id"),
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
        // get user's id
        this.state.id = Cookies.get('id');
        if (!this.state.id) {
            return(
                <div>
                    <h1>Edit Your Profile</h1>
                    <h3>Loading...</h3>
                </div>
            )
        }


      return (
        <div>
        <div className="profile">
          <header>
            <h1>Edit Your Profile</h1>
          </header>

          <Form onSubmit={this.handleSubmit}>
            <Form.Group controlId="profileForm.name">
              <Form.Label>Name</Form.Label>
              <Form.Control name="name" type="input"  placeholder="name namington" value={this.state.name} onChange={this.handleChange} />
            </Form.Group>
            <Form.Group controlId="profileForm.location">
              <Form.Label>Location</Form.Label>
              <Form.Control name="location" as="select" value={this.state.location} onChange={this.handleChange}>
                <option>Baltimore, MD</option>
                <option>Washington DC, DC</option>
                <option>New York City, NY</option>
                <option>Boston, MA</option>
              </Form.Control>
            </Form.Group>
            <FormGroup controlId="profileForm.instrument">
              <Form.Label>Instrument</Form.Label>
              <Form.Check inline name="guitar" label="Guitar" type="checkbox" onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="bass" label="Bass" type="checkbox" onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="drums" label="Drums" type="checkbox" onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="vocals" label="Vocals" type="checkbox" onChange={this.handleInstrumentSelection}/>
            </FormGroup>

            <Button variant="primary" type="submit" onClick={() =>
            {this.submit_form();
            /*this.props.history.push('/myprofile');*/}}>
              Submit
            </Button>

            <Button onClick={() => {this.props.history.push('/myprofile')}}>Go Back</Button>

          </Form>
        </div>

        </div>
      )
    }
  }

  export default EditProfile;
