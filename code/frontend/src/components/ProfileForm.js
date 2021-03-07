import React from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup'


class CreateProfile extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        name: '',
        location: '',
        instruments: []
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
      axios
        .post('https://localhost:3000/musicians', {
          name: this.state.name,
          location: this.state.location,
          instruments: this.state.instruments
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


    render() {
      return (
        <div>
        <div className="profile">
          <h1>Welcome! Let's get you set up.</h1>
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
            <Form.Group>
              <Form.File id="profileForm.profilePicture" label="Upload a profile picture" />
            </Form.Group>
            <Button variant="primary" type="submit">
              Submit
            </Button>
          </Form>
        </div>
        <h2> Testing block </h2>
        <p>{this.state.name}</p>
        <p>{this.state.location}</p>
        <p>{this.state.instruments}</p>
        </div>
      )
    }
  }

  export default CreateProfile;