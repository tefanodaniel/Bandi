import React from 'react';
import { useSelector, shallowEqual } from "react-redux";
import MusicianListItem from './MusicianListItem'
import {Container, Row, Col, ListGroup} from "react-bootstrap";

const selectMusicianIds = (state) => {
    console.log(state)
    return state.musician_reducer.filteredMusicians.map(user => user.id)
}

const MusicianList = () => {
    const musicianIds = useSelector(selectMusicianIds, shallowEqual)
    const renderedListItems = musicianIds.map( (user_id) => {
        return <MusicianListItem key = {user_id} id = {user_id} />
    })
    return <Container >
        <Row className="justify-content-md-left">
            <Col fluid>
                <ListGroup className="justify-content-md-center">{renderedListItems}</ListGroup>
            </Col>
        </Row>
    </Container>
}

export default MusicianList

