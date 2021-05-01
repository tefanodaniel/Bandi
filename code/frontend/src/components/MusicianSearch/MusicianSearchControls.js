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

    let queryparams = {};

    const addnamequery = (e) => {
        let input = e.target.value;
        queryparams.name = input
    }

    const addgenrequery = (e) => {
        let input = e.target.value;
        queryparams.genre = input
    }

    const addexperiencequery = (e) => {
        let input = e.target.value;
        queryparams.experience = input
    }

    const addinstrumentquery = (e) => {
        let input = e.target.value;
        queryparams.instrument = input
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
            console.log('(queryparams)===0')
            dispatch(clearQuery)
        }
        else {
            console.log('submitting new')
            queryparams.id = Cookies.get('id');
            dispatch(newQuery(queryparams))
        }
    }

    return (
        // NEW FORM: 
        /*
        <Form onSubmit={SubmitQuery}>
            <Form.Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <Form.Group controlId="musicianForm.name">
                    <Form.Label>Name:</Form.Label>
                    <Form.Control name="name" type="input" value={queryparams.name} placeholder={placeholder_query.name} />
                </Form.Group>
            </Form.Row>

            <Form.Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <FormGroup controlId="musicianForm.instruments">
                    <Form.Label>Instrument(s):</Form.Label>
                    {['checkbox', 'radio'].map((type) => (
                        <div key={`inline-${type}`} className="mb-3" value={queryparams.instrument}>
                        <Form.Check inline label="Guitar" type={type} id={`inline-${type}-Guitar`} />
                        <Form.Check inline label="Piano" type={type} id={`inline-${type}-Piano`} />
                        <Form.Check inline label="Bass" type={type} id={`inline-${type}-Bass`} />
                        <Form.Check inline label="Drums" type={type} id={`inline-${type}-Drums`} />
                        <Form.Check inline label="Vocals" type={type} id={`inline-${type}-Vocals`} />
                        </div>
                    ))}
                </FormGroup>
            </Form.Row>

            <Form.Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <FormGroup controlId="musicianForm.genres">
                    <Form.Label>Genre(s):</Form.Label>
                        {['checkbox', 'radio'].map((type) => (
                            <div key={`inline-${type}`} className="mb-3" value={queryparams.genre}>
                            <Form.Check inline label="Rock" type={type} id={`inline-${type}-Rock`} />
                            <Form.Check inline label="Blues" type={type} id={`inline-${type}-Blues`} />
                            <Form.Check inline label="Bass" type={type} id={`inline-${type}-Bass`} />
                            <Form.Check inline label="Jazz" type={type} id={`inline-${type}-Jazz`} />
                            <Form.Check inline label="Classical" type={type} id={`inline-${type}-Classical`} />
                            </div>
                        ))}
                </FormGroup>
              </Form.Row>

            <Form.Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <Form.Group controlId="musicianForm.experience">
                    <Form.Label>Experience:</Form.Label>
                    <Form.Control name="experience" as="select" value={queryparams.experience} defaultValue={placeholder_query.experience}>
                        <option>Beginner</option>
                        <option>Intermediate</option>
                        <option>Expert</option>
                    </Form.Control>
                </Form.Group>
            </Form.Row>

            <Form.Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <Form.Group controlId="musicianForm.location">
                    <Form.Label>Location:</Form.Label>
                    <Form.Control name="location" as="select" value={queryparams.location} defaultValue={placeholder_query.location}>
                        <option>Baltimore, MD</option>
                        <option>Washington, DC</option>
                        <option>New York City, NY</option>
                        <option>Boston, MA</option>
                        <option>Los Angeles, CA</option>
                    </Form.Control>
                </Form.Group>
            </Form.Row>

            <Form.Row className="justify-content-sm-left" style={{ marginTop:"50px"}}>
                <FormGroup controlId="musicianForm.distance">
                    <Form.Label>Distance (miles):</Form.Label>
                    <Form.Control name="distance" as="select" value={queryparams.distance} defaultValue={placeholder_query.distance}>
                        <option>10</option>
                        <option>50</option>
                        <option>100</option>
                        <option>500</option>
                        <option>1000</option>
                        <option>3000</option>
                        <option>5000</option>
                    </Form.Control>
                </FormGroup>
            </Form.Row>
            

            <Button type="submit">Find Musicians</Button>

          </Form>
        */

        // OLD:
        
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
                    <h5> Experience :</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {addexperiencequery(e);}} style={{width: "120%"}} placeholder={placeholder_query.experience} type='text'/>
                </div>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
                <Col className="col-sm-5">
                    <h5> State :</h5>
                </Col>
                <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                    <input onChange={e => {addlocationquery(e);}} style={{width: "120%"}} placeholder={placeholder_query.location} type='text'/>
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
                    <Button variant="primary" onClick={SubmitQuery}>Find Musicians</Button>
                </div>
            </Row>
        </Container>
        
    )
}

export default MusicianSearchControls;
