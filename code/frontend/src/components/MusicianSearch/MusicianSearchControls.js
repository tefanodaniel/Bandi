import React from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import { newQuery, clearQuery } from "../../actions/musician_actions";
import { Button } from "react-bootstrap";

const selectPlaceholderQuery = (state) => {
    let query = {};

    if(!state.musician_reducer.query){
        query.name = 'Search by Name';
        query.instrument = 'Search by Instrument';
        query.genre = 'Search by Genre';
        query.distance = 'Search by Distance';
        return query;
    }
    else{
        Object.assign(query, state.musician_reducer.query);
        return query
    }
}

const MusicianSearchControls = () => {
    const dispatch = useDispatch();
    let user = useSelector((state) => state.user_reducer, shallowEqual);
    let placeholder_query = useSelector(selectPlaceholderQuery, shallowEqual);

    let queryparams = {};

    const addnamequery = (e) => {
        let input = e.target.value;
        queryparams.name = input
        console.log('what is the name now,', queryparams.name)
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
                <Col className="col-sm-5">
                    <h5> Name:</h5>
                </Col>
                <Col className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                       <input onChange={e => {addnamequery(e);}} style={{width: "120%"}} placeholder={placeholder_query.name} type='text'/>
                </Col>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <h5> Instrument:</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {addinstrumentquery(e);}} style={{width: "120%"}} placeholder={placeholder_query.instrument} type='text'/>
                </div>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Genre :</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {addgenrequery(e);}} style={{width: "120%"}} placeholder={placeholder_query.genre} type='text'/>
                </div>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> Within Distance (miles) :</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {adddistancequery(e);}} style={{width: "120%"}} placeholder={placeholder_query.distance} type='text'/>
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

export default MusicianSearchControls;
