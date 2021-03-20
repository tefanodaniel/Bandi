import React from 'react';
import { useDispatch } from 'react-redux';
import {newNameQuery} from "./MusicianBrowse";
import {Container, Row} from "react-bootstrap";
import FormGroup from "react-bootstrap/FormGroup";
import Form from "react-bootstrap/Form";
import {newQuery, clearQuery} from "./MusicianBrowse";
import {Button} from "react-bootstrap";

const MusicianSearch = () => {
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
        console.log('at the time of submit', queryparams);
        console.log('at the time of submit the length', queryparams.length);
        if(Object.keys(queryparams)===0) {
            dispatch(clearQuery)
        }
        else {
            dispatch(newQuery(queryparams))
        }
    }

    return (
        <Container>
            <Row>
                <div className='control' style={{minWidth: "300px", textAlign:"center"}}>
                    <h6> Name : </h6> <input onChange={e => {addnamequery(e);}} style={{width: "100%"}} placeholder='Search by Name' type='text'/>
                </div>
            </Row>
            <Row>
                <div className='control' style={{minWidth: "300px", textAlign:"center"}}>
                    <h6> Instrument : </h6>
                    <input onChange={e => {addinstrumentquery(e);}} style={{width: "100%"}} placeholder='Search by Instrument' type='text'/>
                </div>
                <div> </div>
            </Row>
            <Row>
                <div className='control' style={{minWidth: "300px", textAlign:"center"}}>
                    <h6> Genre : </h6>
                    <input onChange={e => {addgenrequery(e);}} style={{width: "100%"}} placeholder='Search by Genre' type='text'/>
                </div>
                <div> </div>
            </Row>
            <div> </div>
            <Button variant="primary" onClick={SubmitQuery}>Submit!</Button>
        </Container>
    )
}

export default MusicianSearch

