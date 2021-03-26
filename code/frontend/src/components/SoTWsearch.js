import React from 'react';
import { useDispatch } from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import {newQuery, clearQuery} from "../features/browse/MusicianBrowse";
import {Button, Form} from "react-bootstrap";

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

    return (
        <Container fluid>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <h5> Week:</h5>
                </Col>
                <Form className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <Form.Group controlId="exampleForm.SelectCustom">
                        <Form.Control as="select" custom>
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                            <option>4</option>
                            <option>5</option>
                        </Form.Control>
                    </Form.Group>
                </Form>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <h5> Instrument:</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {addinstrumentquery(e);}} style={{width: "120%"}} placeholder='Search by Instrument' type='text'/>
                </div>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
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

