import React from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
//import { newQuery, clearQuery } from "../actions/band_actions";
import { Button } from "react-bootstrap";

const BandSearch = () => {
    const dispatch = useDispatch();
    let user = useSelector((state) => state.user_reducer, shallowEqual);
    let queryparams = {};

    const addnamequery = (e) => {
        let input = e.target.value;
        queryparams.name = input
    }

    const addgenrequery = (e) => {
        let input = e.target.value;
        queryparams.genre = input
    }

    const adddistancequery = (e) => {
        let input = e.target.value;
        queryparams.distance = input
    }

    const addcapacityquery = (e) => {
        let input = e.target.value;
        queryparams.capacity = input
    }

    const SubmitQuery = () => {
        if(Object.keys(queryparams)===0) {
            dispatch(clearQuery)
        } else {
            queryparams.id = user.id
            dispatch(newQuery(queryparams))
        }
    }

    return (
    /** Add formatting for the band search on the webpage
    */
        <Container fluid>
            <Row className="justify-content-sm-left" style={{marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <h5> Name:</h5>
                </Col>
                <Col className="justify-content-sm-left" style={{minWidth: "175px", textAlign: "center"}}>
                    <input onChange={e => {addnamequery(e);}} style={{width: "120%"}} placeholder='Search by Name' type='text'/>
                </Col>
            </Row>
            <Row className="justify-content-sm-left" style={{marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <h5> Genre:</h5>
                </Col>
                <Col>
                    <div className="col-sm-7" style={{minWidth:"175px", textAlign:"center"}}>
                        <input onChange={e => {addgenrequery(e);}} style={{width:"120%"}} placeholder='Search by Genre' type='text'/>
                    </div>
                </Col>
            </Row>
            <Row className="justify-content-sm-left" style={{marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <h5 Within Distance (miles):</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth:"175px", textAlign:"center"}}>
                    <input onChange={e =>{adddistancequery(e);}} style={{width:"120%"}} placeholder='500' type='text'/>
                </div>
            </Row>
            <Row className="justify-content-sm-left" style={{marginTop:"20px"}}>
                <Col className="col-sm-5">
                </Col>
                <div className="col-sm-7" style={{textAlign: "center"}}>
                    <Button variant="primary" onClick={SubmitQuery}>Submit</Button>
                </div>
            </Row>
        </Container>
    )

}

export default BandSearch