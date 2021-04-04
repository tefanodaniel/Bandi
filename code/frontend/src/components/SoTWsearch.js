import React from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import { newQuery, clearQuery } from "../actions/musician_actions";
import {Button, Form} from "react-bootstrap";
import {getCurrentEvent, getCurrentEventSong, getCurrentEventSubmissions} from "../actions/sotw_event_actions";
var startOfWeek = require('date-fns/startOfWeek');
var endOfWeek = require('date-fns/endOfWeek');

const selectSongId = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event.songId
}


const SoTWsearch = () => {
    const dispatch = useDispatch();
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

    const SubmitQuery = () => {
        if(Object.keys(queryparams)===0) {
            dispatch(clearQuery)
        }
        else {
            dispatch(newQuery(queryparams))
        }
    }

    //Ideally should display last x weeks or have the calendar pop-up.
    var start = startOfWeek(new Date());
    start = start.toString().split(' ').slice(0, 3).join(' ');
    var end = endOfWeek(new Date());
    end = end.toString().split(' ').slice(0, 3).join(' ');
    let eventId = "00001fakeeventid";
    dispatch(getCurrentEvent(eventId))

    let songId = useSelector(selectSongId, shallowEqual);
    dispatch(getCurrentEventSong(songId))
    dispatch(getCurrentEventSubmissions(eventId))

    return (
        <Container fluid>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-3">
                    <h5>Week:</h5>
                </Col>
                <Form className="col-sm-7" style={{minWidth: "300px", textAlign:"center"}}>
                    <Form.Group controlId="exampleForm.SelectCustom">
                        <Form.Control as="select" custom>
                            <option>{start} - {end}</option>
                        </Form.Control>
                    </Form.Group>
                </Form>
            </Row>
        </Container>
    )
}

export default SoTWsearch
/*
The search functionality for Song of the week hasn't been implemented yet.
<Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
<Col className="col-sm-3">
    <h5> Instrument:</h5>
</Col>
<div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
<input onChange={e => {addinstrumentquery(e);}} style={{width: "120%"}} placeholder='Search by Instrument' type='text'/>
</div>
</Row>
<Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
<Col className="col-sm-3">
<h5> Genre :</h5>
</Col>
<div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
<input onChange={e => {addgenrequery(e);}} style={{width: "120%"}} placeholder='Search by Genre' type='text'/>
</div>
</Row>
<Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
<Col className="col-sm-5">
</Col>
<div className="col-sm-7" style={{textAlign:"center"}}>
<Button variant="primary" onClick={SubmitQuery} >Submit!</Button>
</div>
</Row>

*/
