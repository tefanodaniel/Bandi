import React from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup'
import Cookies from "js-cookie";
import {getBackendURL} from "../utils/api";

import { connect } from 'react-redux';
import { updateMusicianProfile } from '../actions/musician_actions';
import { getUser } from '../actions/user_actions';

class EditProfile extends React.Component {
    constructor(props) {
      super(props);

      const userInfo = this.props.store.user_reducer;
      console.log(userInfo);
      //console.log(userInfo.instruments, userInfo.instruments.length);

      this.state = {

        id : Cookies.get('id'),

        name: userInfo.name,
        location: userInfo.location,
        experience: userInfo.experience,
        instruments: userInfo.instruments,
        genres: userInfo.instruments
      }

      this.handleSubmit = this.handleSubmit.bind(this);
      this.handleChange = this.handleChange.bind(this);
      this.handleInstrumentSelection = this.handleInstrumentSelection.bind(this);
      this.handleGenreSelection = this.handleGenreSelection.bind(this);
      this.instrumentIsChecked = this.instrumentIsChecked.bind(this);
      this.genreIsChecked = this.genreIsChecked.bind(this);
    }

    handleChange(event) {
      const target = event.target;
      let value = target.value;
      const name = target.name;
      if (name === "experience" && value === "Select skill level") {
        value = "";
      } else if (name === "location" && value === "Select location") {
        value = "";
      }
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

    handleSubmit() {
      // Prevent refresh of the page
      event.preventDefault();

      const data = {
        id: this.state.id,
        name: this.state.name,
        location: this.state.location,
        experience: this.state.experience,
        instruments: this.state.instruments,
        genres: this.state.genres
      }

      // Send PUT request to our API
      const { updateMusicianProfile, getUser } = this.props;
      updateMusicianProfile(data);

      // GET our updated user to update the redux store
      getUser(data.id);

      // Redirect back to view the updated profile
      this.props.history.push('/myprofile')
    }

    // submit_form() {
    //   const axios = require('axios')
    //   const url = getBackendURL() + "/musicians" + "/" + Cookies.get("id");
    //
    //   axios
    //       .put(url, {
    //           id: this.state.id,
    //         name: this.state.name,
    //           location: this.state.location,
    //           experience: this.state.experience,
    //           instruments: this.state.instruments,
    //           genres: this.state.genres
    //       })
    //       .then(res => {
    //         console.log(`statusCode: ${res.statusCode}`)
    //         console.log(res)
    //       })
    //       .catch(error => {
    //         console.error(error)
    //       })
    // }

    instrumentIsChecked(instrument) {
      const userInfo = this.props.store.user_reducer;
      return userInfo.instruments && userInfo.instruments.includes(instrument);
    }

    genreIsChecked(genre) {
      const userInfo = this.props.store.user_reducer;
      return userInfo.genres && userInfo.genres.includes(genre);
    }

    render() {
      // Check if user is logged in, else redirect to the main page
      if (!this.state.id) {
          this.props.history.push('/discover');
      }



      return (
        <div>
        <div className="profile">
          <header>
            <h1>Edit Your Profile</h1>
          </header>

          <Form onSubmit={this.handleSubmit}>

            <Form.Group controlId="profileForm.name">
              <Form.Label>Name:</Form.Label>
              <Form.Control name="name" type="input"  placeholder="Name" value={this.state.name} onChange={this.handleChange} />
            </Form.Group>

            <Form.Group controlId="profileForm.location">
              <Form.Label>Location:</Form.Label>
              <Form.Control name="location" as="select" value={this.state.location} onChange={this.handleChange}>
                <option>Select location</option>
                <option>Baltimore, MD</option>
                <option>Washington, DC</option>
                <option>New York City, NY</option>
                <option>Boston, MA</option>
                <option>Los Angeles, CA</option>
                <option>London, UK</option>
              </Form.Control>
            </Form.Group>

              <Form.Group controlId="profileForm.experience">
                  <Form.Label>Experience:</Form.Label>
                  <Form.Control name="experience" as="select" value={this.state.experience} onChange={this.handleChange}>
                      <option>Select skill level</option>
                      <option>Beginner</option>
                      <option>Intermediate</option>
                      <option>Expert</option>
                  </Form.Control>
              </Form.Group>

            <FormGroup controlId="profileForm.instruments">
              <Form.Label>Instruments:</Form.Label>
              <Form.Check inline name="guitar" label="Guitar" type="checkbox" defaultChecked={this.instrumentIsChecked("guitar")} onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="bass" label="Bass" type="checkbox" defaultChecked={this.instrumentIsChecked("bass")} onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="drums" label="Drums" type="checkbox" defaultChecked={this.instrumentIsChecked("drums")} onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="vocals" label="Vocals" type="checkbox" defaultChecked={this.instrumentIsChecked("vocals")} onChange={this.handleInstrumentSelection}/>
              <Form.Check inline name="piano" label="Piano / Keyboard" type="checkbox" defaultChecked={this.instrumentIsChecked("piano")} onChange={this.handleInstrumentSelection}/>
            </FormGroup>

              <FormGroup controlId="profileForm.genres">
                  <Form.Label>Genres:</Form.Label>
                  <Form.Check inline name="rock" label="Rock" type="checkbox" defaultChecked={this.genreIsChecked("rock")} onChange={this.handleGenreSelection}/>
                  <Form.Check inline name="blues" label="Blues" type="checkbox" defaultChecked={this.genreIsChecked("blues")} onChange={this.handleGenreSelection}/>
                  <Form.Check inline name="jazz" label="Jazz" type="checkbox" defaultChecked={this.genreIsChecked("jazz")} onChange={this.handleGenreSelection}/>
                  <Form.Check inline name="classical" label="Classical" type="checkbox" defaultChecked={this.genreIsChecked("classical")} onChange={this.handleGenreSelection}/>
              </FormGroup>

            <Button type="submit">Save</Button>

          </Form>
        </div>

        </div>
      )
    }
  }

//  export default EditProfile;

function mapStateToProps(state) {
  return {
    store: state
  };
} // end mapStateToProps
export default connect(mapStateToProps, {updateMusicianProfile, getUser})(EditProfile);
