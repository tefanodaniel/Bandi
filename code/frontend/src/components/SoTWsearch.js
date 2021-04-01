import React from 'react';
import { useDispatch } from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import { newQuery, clearQuery } from "../actions/musician_actions";
import {Button, Form} from "react-bootstrap";
var startOfWeek = require('date-fns/startOfWeek');
var endOfWeek = require('date-fns/endOfWeek');


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
                            <option>Last Week</option>
                        </Form.Control>
                    </Form.Group>
                </Form>
            </Row>
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
        </Container>
    )
}

export default SoTWsearch

