import 'react-tabs/style/react-tabs.css';
import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import {logout} from "../utils/api";
import {connect} from 'react-redux';
import Form from "react-bootstrap/Form";
import FormGroup from "react-bootstrap/FormGroup";
import { Container, Row, Col } from "react-bootstrap";
import MusicianList from "./MusicianList";
import MusicianSearch from "../features/browse/MusicianSearch";

class SearchMusician extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        if(true) {
            return (
                <div>
                    <nav className="navbar navbar-expand navbar-dark bg-dark">
                        <Link to={"/"} className="navbar-brand">
                            banDi
                        </Link>
                        <div className="navbar-nav mr-auto">
                            <li className="nav-item">
                                <Link to={"/profile"} className="nav-link">
                                    My Profile
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to={"/signin"} onClick={logout} className="nav-link">
                                    Log Out
                                </Link>
                            </li>
                        </div>
                    </nav>
                    <Container>
                        <h3 style={{textAlign:'center'}}> Welcome to BanDi's Search. </h3>
                        <h6 style={{textAlign:'center'}}> Find your musical soulmate here. </h6>
                    </Container>
                    <MusicianSearch />
                    <div></div>
                    <MusicianList />
                </div>
            );
        }
        else {
            return (
                <div>
                    <h1> Here is your search! </h1>
                    <h3> Loading ... </h3>

                </div>
            );
        }
    }
}

function mapStateToProps(state) {
    return {state};
}

export default connect(mapStateToProps)(SearchMusician);

