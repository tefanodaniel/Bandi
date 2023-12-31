import React from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import { newQuery, clearQuery } from "../../actions/band_actions";
import { Button } from "react-bootstrap";
import {selectPlaceholderQuery} from "../../selectors/band_selector";
import { getLoggedInUser } from "../../selectors/user_selector";

const BandSearchControls = () => {
    const dispatch = useDispatch();
    let user = useSelector(getLoggedInUser, shallowEqual);
    let placeholder_query = useSelector(selectPlaceholderQuery, shallowEqual);

    let queryparams = {};

    const addnamequery = (e) => {
        let input = e.target.value;
        queryparams.name = input
        console.log('what is the current name,', queryparams.name)
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
        if (Object.keys(queryparams)===0) {
            dispacth(clearQuery)
        } else {
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
                                <h5> Capacity:</h5>
                            </Col>
                            <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                                <input onChange={e => {addcapacityquery(e);}} style={{width: "120%"}} placeholder={placeholder_query.capacity} type='text'/>
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