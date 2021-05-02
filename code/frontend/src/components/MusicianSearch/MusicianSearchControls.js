import React from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import { newQuery, clearQuery} from "../../actions/musician_actions";
import { Button } from "react-bootstrap";
import '../../styles/musician_search.css';
import { getLoggedInUser } from '../../selectors/user_selector';
import {selectPlaceholderQuery} from "../../selectors/musician_selector";
import Cookies from "js-cookie";

const MusicianSearchControls = () => {
    const dispatch = useDispatch();
    let placeholder_query = useSelector(selectPlaceholderQuery, shallowEqual);

    let queryparams = {
        genre: [],
        instrument: []
    };

    const addnamequery = (e) => {
        let input = e.target.value;
        queryparams.name = input;
    }

    const addgenrequery = (e) => {
        let input = e.target.name;
        if (e.target.checked) {
            queryparams.genre.push(input);
        }
        else { // remove query param if box is unchecked:
            let index = queryparams.genre.indexOf(input);
            if(index !== -1) {queryparams.genre.splice(index, 1)}
        }
    }

    const addexperiencequery = (e) => {
        let input = e.target.value;
        queryparams.experience = input
    }

    const addinstrumentquery = (e) => {
        let input = e.target.name;
        if (e.target.checked) {
            queryparams.instrument.push(input);
        }
        else { // remove query param if box is unchecked:
            let index = queryparams.instrument.indexOf(input);
            if(index !== -1) {queryparams.instrument.splice(index, 1)}
        }
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
            dispatch(clearQuery)
        }
        else {
            queryparams.id = Cookies.get('id');
            for(var query in queryparams) {
                if (queryparams[query] === '') {
                    delete queryparams[query];
                }
            }
            var inputs=document.getElementsByTagName("input");
            for (var i in inputs) {
                if (inputs[i].type=="checkbox") inputs[i].checked=false;
                if (inputs[i].type=="text") inputs[i].value='';
            }
            dispatch(newQuery(queryparams));
        }
    }

    return (
        <Container fluid>
            <Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <h5>Name:</h5>
                <input id="name-input" onChange={e => {addnamequery(e);}} placeholder="Search by Name" type='text'/>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <h5>Instrument:</h5>
                <label>Guitar
                  <input name="Guitar" onChange={e => {addinstrumentquery(e);}} type="checkbox" />
                </label>
                <label>Piano
                  <input name="Piano" onChange={e => {addinstrumentquery(e);}} type="checkbox" />
                </label>

                <label>Bass
                  <input name="Bass" onChange={e => {addinstrumentquery(e);}} type="checkbox" />
                </label>
                <label>Drums
                  <input name="Drums" onChange={e => {addinstrumentquery(e);}} type="checkbox" />
                </label>
                <label>Vocals
                  <input name="Vocals" onChange={e => {addinstrumentquery(e);}} type="checkbox" />
                </label>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <h5>Genre:</h5>
                 <label>Rock
                    <input name="Rock" onChange={e => {addgenrequery(e);}} type="checkbox" />
                  </label>
                  <label>Blues
                    <input name="Blues" onChange={e => {addgenrequery(e);}} type="checkbox" />
                  </label>
                  <label>Jazz
                  <input name="Jazz" onChange={e => {addgenrequery(e);}} type="checkbox" />
                  </label>
                  <label>Classical
                    <input name="Classical" onChange={e => {addgenrequery(e);}} type="checkbox" />
                  </label>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Experience</h5>
                </Col>
                <select className="col-sm-7" value={queryparams.experience} onChange={e => {addexperiencequery(e);}}>
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
                    <option value="New York City, NY">New York City, NY</option>
                    <option value="Boston, MA">Boston, MA</option>
                    <option value="Los Angeles, CA">Los Angeles, CA</option>
                </select>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Within Distance (miles)</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {adddistancequery(e);}} placeholder="30" type='text'/>
                </div>
            </Row>
            <Row id="submit-button-row" className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <button id="musician-search-submit" class="bandi-button" onClick={SubmitQuery} >Find musicians!</button>
            </Row>
        </Container>
        
    )
}

export default MusicianSearchControls;
