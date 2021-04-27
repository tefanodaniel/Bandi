import React from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup'
import Cookies from "js-cookie";
import {getBackendURL} from "../../utils/api";
import Header from "../Header/Header";
import Footer from "../Footer";

import { connect } from 'react-redux';
import { updateUserProfile, getUser } from '../../actions/user_actions';

class EditUserInfo extends React.Component {
    constructor(props) {
      super(props);

      const userInfo = this.props.store.user_reducer;

      this.state = {

        id : Cookies.get('id'),

        name: userInfo.name,
        location: userInfo.location,
        experience: userInfo.experience,
        instruments: userInfo.instruments,
        genres: userInfo.genres
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
      const field = target.name;
      if (field === "experience" && value === "Select skill level") {
        value = "";
      } else if (field === "location" && value === "Select location") {
        value = "";
      } else if (field === "instruments" && value === "Select instrument") {
        value = "";
      } else if (field === "genre" && value === "Select genre") {
        value = "";
      }
      this.setState({
        [field]: value
      });
    }


    handleInstrumentSelection(event) {
      const target = event.target;
      const instrument = target.name;
      let newInstrumentList = this.state.instruments.slice();
      if (target.checked) {
        newInstrumentList.push(instrument);
      } else {
        const index = newInstrumentList.indexOf(instrument);
        if (index > -1) { newInstrumentList.splice(index, 1); }
      }
      this.setState({
        instruments: newInstrumentList
      });
    }

    handleGenreSelection(event) {
        const target = event.target;
        const genre = target.name;
        let newGenreList = this.state.genres.slice();
        if (target.checked) {
            newGenreList.push(genre);
        } else {
            const index = newGenreList.indexOf(genre);
            if (index > -1) { newGenreList.splice(index, 1); }
        }
        this.setState({
            genres: newGenreList
        });
    }

    handleSubmit(event) {
      // Prevent refresh of the page
      event.preventDefault();

      const userInfo = this.props.store.user_reducer;
      const formFields = ["id", "name", "location", "experience", "instruments", "genres"];
      const formData = {
        id: this.state.id
      }

      // Create dictionary of only the profile fields that have changed
      for (let i = 0; i < formFields.length; i++) {
        let key = formFields[i]
        if (key === "instruments" || key === "genres") {
          if (userInfo[key].join() !== this.state[key].join()) {
            formData[key] = this.state[key];
          }
        } else {
          if (userInfo[key] !== this.state[key]) {
            formData[key] = this.state[key];
          }
        }

      }

      if (Object.keys(formData).length > 1) {
        // Send PUT request to our API
        const { updateUserProfile, getUser } = this.props;
        updateUserProfile(formData);
      }

      // Redirect back to view the updated profile
      this.props.history.push('/myprofile')
    }

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
                    <Form.Control name="instruments" as="select" value={this.state.instruments} onChange={this.handleChange}>
                        <option>Select instrument</option>
                        <option>Guitar</option>
                        <option>Piano</option>
                        <option>Bass</option>
                        <option>Drums</option>
                        <option>Vocals</option>
                    </Form.Control>
                </FormGroup>

            <FormGroup controlId="profileForm.genres">
                  <Form.Label>Genres:</Form.Label>
                  <Form.Control name="genre" as="select" value={this.state.genre} onChange={this.handleChange}>
                    <option>Select genre</option>
                    <option>Rock</option>
                    <option>Blues</option>
                    <option>Jazz</option>
                    <option>Classical</option>
                  </Form.Control>
              </FormGroup>

              <Form.Group controlId="profileForm.links">
                  <Form.Label>Links:</Form.Label>
                  <Form.Control name="links" type="input" placeholder="Links" value={this.state.links} onChange={this.handleChange} />
              </Form.Group>

            <Button type="submit">Save</Button>

          </Form>
        </div>

        </div>
      )
    }
  }

function mapStateToProps(state) {
  return {
    store: state
  };
} // end mapStateToProps
export default connect(mapStateToProps, {updateUserProfile, getUser})(EditUserInfo);
