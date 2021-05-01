import React from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col, Spinner} from "react-bootstrap";
import Form from 'react-bootstrap/Form';
import FormGroup from 'react-bootstrap/FormGroup';
import { newQuery, clearQuery } from "../../actions/musician_actions";
import { Button } from "react-bootstrap";
import {selectPlaceholderQuery} from "../../selectors/musician_selector";
import { getLoggedInUser } from "../../selectors/user_selector";
import Cookies from "js-cookie";

const MusicianSearchControls = () => {
    const dispatch = useDispatch();
    let user = useSelector(getLoggedInUser, shallowEqual);
    let placeholder_query = useSelector(selectPlaceholderQuery, shallowEqual);

    let queryparams = {
        genre: [],
        instrument: []
    };

    let instruments = {};
    let genres = {};

    const addnamequery = (e) => {
        let input = e.target.value;
        queryparams.name = input
    }

    const addgenrequery = (e) => {
        let input = e.target.name;
        if (e.target.checked) {
            queryparams.genre.push(input);
        }
        else { // remove query param if box is unchecked
            let index = queryparams.genre.indexOf(input);
            if(index !== -1) {queryparams.genre.splice(index, 1)}
        }
    }

    const addexperiencequery = (e) => {
        let input = e.target.value;
        queryparams.experience = input
    }

    const addinstrumentquery = (e) => {
        // e.target.checked <-- will tell us if box is checked or not (may want to save to state)
        let input = e.target.name;
        if (e.target.checked) {
            queryparams.instrument.push(input);
        }
        else { // remove query param if box is unchecked
            let index = queryparams.instrument.indexOf(input);
            if(index !== -1) {queryparams.instrument.splice(index, 1)}
        }
        console.log("params are: ", queryparams);
    }

    const addlocationquery = (e) => {
        let input = e.target.value;
        queryparams.location = input
    }

    const adddistancequery = (e) => {
        let input = e.target.value;
        queryparams.distance = input
    }

    const SubmitQuery = () => {
        if(Object.keys(queryparams)===0) {
            //console.log('(queryparams)===0')
            dispatch(clearQuery)
        }
        else {
            //console.log('submitting new')
            queryparams.id = Cookies.get('id');
            for(var query in queryparams) {
                if (queryparams[query] === '') {
                    delete queryparams[query];
                }
            }
            dispatch(newQuery(queryparams))
        }
    }

    return (
        <Container fluid>
            <Row className="justify-content-sm-left" style={{ marginLeft:"0px"}}>
                <h4>Musician Filters</h4>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"10px"}}>
                <Col className="col-sm-5">
                    <h5> Name</h5>
                </Col>
                <Col className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                       <input onChange={e => {addnamequery(e);}} style={{width: "120%"}} placeholder={placeholder_query.name} type='text'/>
                </Col>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <h5> Instrument</h5>
                </Col>
                <Col className="col-sm-7" style={{width: "120%", textAlign:"left"}}>
                    <div className="col-sm-7" style={{textAlign:"left"}}>
                        <label>Guitar
                            <input name="Guitar" onChange={e => {addinstrumentquery(e);}} type="checkbox" checked={instruments.guitar}/>
                        </label>
                    </div>
                    <div className="col-sm-7" style={{textAlign:"left"}}>
                        <label>Piano
                            <input name="Piano" onChange={e => {addinstrumentquery(e);}} type="checkbox" checked={instruments.piano}/>
                        </label>
                    </div>
                    <div className="col-sm-7" style={{textAlign:"left"}}>
                        <label>Bass
                            <input name="Bass" onChange={e => {addinstrumentquery(e);}} type="checkbox" checked={instruments.bass}/>
                        </label>
                    </div>
                    <div className="col-sm-7" style={{textAlign:"left"}}>
                        <label>Drums
                            <input name="Drums" onChange={e => {addinstrumentquery(e);}} type="checkbox" checked={instruments.drums}/>
                        </label>
                    </div>
                    <div className="col-sm-7" style={{textAlign:"left"}}>
                        <label>Vocals
                            <input name="Vocals" onChange={e => {addinstrumentquery(e);}} type="checkbox" checked={instruments.bass}/>
                        </label>
                    </div>
                </Col>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Genre</h5>
                </Col>
                <Col className="col-sm-7" style={{width: "120%", textAlign:"left"}}>
                    <div className="col-sm-7" style={{textAlign:"left"}}>
                        <label>Rock
                            <input name="Rock" onChange={e => {addgenrequery(e);}} type="checkbox" checked={genres.rock}/>
                        </label>
                    </div>
                    <div className="col-sm-7" style={{textAlign:"left"}}>
                        <label>Blues
                            <input name="Blues" onChange={e => {addgenrequery(e);}} type="checkbox" checked={instruments.blues}/>
                        </label>
                    </div>
                    <div className="col-sm-7" style={{minWidth: "175px", textAlign:"left"}}>
                        <label>Jazz
                            <input name="Jazz" onChange={e => {addgenrequery(e);}} type="checkbox" checked={instruments.jazz}/>
                        </label>
                    </div>
                    <div className="col-sm-7" style={{minWidth: "175px", textAlign:"left"}}>
                        <label>Classical
                            <input name="Classical" onChange={e => {addgenrequery(e);}} type="checkbox" checked={instruments.classical}/>
                        </label>
                    </div>
                </Col>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Experience</h5>
                </Col>
                <select className="col-sm-7" value={queryparams.experience} style={{width: "200%"}} onChange={e => {addexperiencequery(e);}}>
                    <option value="">Select Skill level</option>
                    <option value="Beginner">Beginner</option>
                    <option value="Intermediate">Intermediate</option>
                    <option value="Expert">Expert</option>
                </select>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Location</h5>
                </Col>
                <select className="col-sm-7" value={queryparams.location} style={{width: "120%"}} onChange={e => {addlocationquery(e);}}>
                    <option value="">Select Location</option>
                    <option value="Baltimore, MD">Baltimore, MD</option>
                    <option value="Washington, DC">Washington, DC</option>
                    <option value="ew York City, NY">New York City, NY</option>
                    <option value="Boston, MA">Boston, MA</option>
                    <option value="Los Angeles, CA">Los Angeles, CA</option>
                </select>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Within Distance (miles)</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {adddistancequery(e);}} style={{width: "120%"}} placeholder={placeholder_query.distance} type='text'/>
                </div>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-5">
                </Col>
                <div className="col-sm-7" style={{textAlign:"center"}}>
                    <Button variant="primary" onClick={SubmitQuery}>Find Musicians</Button>
                </div>
            </Row>
        </Container>
        
    )
}

export default MusicianSearchControls;
