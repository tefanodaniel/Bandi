import React from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import { newQuery, clearQuery } from "../../actions/musician_actions";
import { Button } from "react-bootstrap";
import '../../styles/musician_search.css';
import { getLoggedInUser } from '../../selectors/user_selector';

const selectPlaceholderQuery = (state) => {
    let query = {};

    if(!state.musician_reducer.query){
        query.name = ' ex: Slash';
        query.instrument = ' ex: guitar';
        query.genre = ' ex: rock';
        query.distance = ' ex: 15';
        return query;
    }
    else{
        Object.assign(query, state.musician_reducer.query);
        return query
    }
}

const MusicianSearchControls = () => {
    const dispatch = useDispatch();
    let user = useSelector(getLoggedInUser, shallowEqual);
    let placeholder_query = useSelector(selectPlaceholderQuery, shallowEqual);

    let queryparams = {};

    const addnamequery = (e) => {
        let input = e.target.value;
        queryparams.name = input
    }

    const addgenrequery = (e) => {
        let input = e.target.value;
        queryparams.genre = input
    }

    const addinstrumentquery = (e) => {
        let input = e.target.value;
        queryparams.instrument = input
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
            queryparams.id = user.id
            dispatch(newQuery(queryparams))
        }
    }

    return (
        <Container fluid>
            <Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <h5>Name:</h5>
                <input id="name-input" onChange={e => {addnamequery(e);}} placeholder={placeholder_query.name} type='text'/>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <h5>Instrument:</h5>
                <input id="instrument-input" onChange={e => {addinstrumentquery(e);}} placeholder={placeholder_query.instrument} type='text'/>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <h5>Genre:</h5>
                    <input id="genre-input" onChange={e => {addgenrequery(e);}} placeholder={placeholder_query.genre} type='text'/>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <h5>Distance (miles):</h5>
                <input id="distance-input" onChange={e => {adddistancequery(e);}} placeholder={placeholder_query.distance} type='text'/>
            </Row>
            <Row id="submit-button-row" className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <button id="musician-search-submit" class="bandi-button" onClick={SubmitQuery} >Submit!</button>
            </Row>
        </Container>
    )
}

export default MusicianSearchControls;
