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
      this.handleGenreSelection = this.handleGenreSelection.bind(this);
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

    handleSubmit(event) {

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
            this.props.history.push('/discover');
        }

      return (
        <div>
        <div className="profile">
          <header>
            <h1>Edit Your Profile</h1>
          </header>

          <Form onSubmit={this.submit_form()}>

            <Form.Group controlId="profileForm.name">
              <Form.Label>Name</Form.Label>
              <Form.Control name="name" type="input"  placeholder="name namington" value={this.state.name} onChange={this.handleChange} />
            </Form.Group>

            <Form.Group controlId="profileForm.location">
              <Form.Label>Location</Form.Label>
              <Form.Control name="location" as="select" value={this.state.location} onChange={this.handleChange}>
                <option>Baltimore, MD</option>
                <option>Washington, DC</option>
                <option>New York City, NY</option>
                <option>Boston, MA</option>
                  <option>Los Angeles, CA</option>
                  <option>London, UK</option>
              </Form.Control>
            </Form.Group>

            <FormGroup controlId="profileForm.instruments">
              <Form.Label>Instruments:</Form.Label>
              <Form.Check inline name="guitar" label="Guitar" type="checkbox" onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="bass" label="Bass" type="checkbox" onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="drums" label="Drums" type="checkbox" onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="vocals" label="Vocals" type="checkbox" onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="piano" label="Piano / Keyboard" type="checkbox" onChange={this.handleInstrumentSelection}/>
            </FormGroup>

              <FormGroup controlId="profileForm.genres">
                  <Form.Label>Genres:</Form.Label>
                  <Form.Check inline name="rock" label="Rock" type="checkbox" onChange={this.handleGenreSelection}/>
                  <Form.Check inline name="blues" label="Blues" type="checkbox" onChange={this.handleGenreSelection}/>
                  <Form.Check inline name="jazz" label="Jazz" type="checkbox" onChange={this.handleGenreSelection}/>
                  <Form.Check inline name="classical" label="Classical" type="checkbox" onChange={this.handleGenreSelection}/>
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
