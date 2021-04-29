import Header from "./Header";
import Footer from "./Footer";
import Form from 'react-boostrap/Form';
import Button from 'react-bootstrap/Button';
import FormGroup from 'react-bootstrap/FormGroup';
import {getBackendURL} from "../utils/api";

import React, {Component} from 'react';
import Select from 'react-select'

class ContactUs extends React.Component {
    constructor() {
        super();
        /** starting state for contact form
        */
        this.state = {
            name: "",
            email: "",
            message: "",
            status: "Submit"
        };
    }

    /** Handles filling in the form
    */
    handleChange(event) {
        const field = event.target.id;
        if (field === "name") {
            this.setState({ name: event.target.value });
        } else if (field === "email") {
            this.setState({ email: event.target.value });
        } else if (field === "message") {
            this.setState({ message: event.target.value });
        }
    }

    /** Handles the submission of the form, currently nonfunctional
     *  because its meant to work on localhost, figure out how to
     *  handle email requests from heroku
     */
    handleSubmit(event) {
        event.preventDefault();
        /*this.setState({ status: "Sending" });
        axios({
            method: "POST",
            url: "http://localhost:5000/contact",
            data: this.state,
        }).then((response) => {
            if (response.data.status === "sent") {
                alert("Message Sent");
                this.setState({ name: "", email: "", message: "", status: "Submit" });
            } else if (response.data.status === "failed") {
                alert("Message Failed");
            }
        });*/
    }

    /** Render the form for email requests
     */
    render() {
        let buttonText = this.state.status;
        return(
            <form onSubmit={this.handleSubmit.bind(this)} method="POST">4
                <div>
                <label htmlFor="name">Name:</label>
                <input type="text"
                    id="name"
                    value={this.state.name}
                    onChange={this.handleChange.bind(this)}
                    required/>
                </div>
                <div>
                <label htmlFor="email">Email:</label>
                <input type="email"
                    id="email"
                    value={this.state.email}
                    onChange={this.handleChange.bind(this)}
                    required/>
                </div>
                <div>
                <label htmlFor="message">Message:</label>
                <textarea id="message"
                    value={this.state.message}
                    onChange={this.handleChange.bind(this)}
                    required/>
                </div>
                <button type="submit">{buttonText}</button>
        );
    }
}

export default ContactUs;