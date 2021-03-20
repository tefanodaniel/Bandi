import React from "react";
import { useSelector } from "react-redux";
import {Card, ListGroup } from "react-bootstrap";

const selectMusiciansById = (state, user_id) => {
    return state.mreducer.filteredMusicians.find((user) => user.id === user_id)
}

const MusicianListItem = ({ id }) => {
    const musician = useSelector((state) => selectMusiciansById(state, id))

    return (
        <ListGroup.Item>
            <Card style={{ minwidth: '18rem' , maxwidth: '18rem'}}>
                <Card.Body>
                    <Card.Title>Name: {musician.name}</Card.Title>
                    <Card.Text>Genres: {musician.genres.join(', ')}</Card.Text>
                    <Card.Text>Instruments: {musician.instruments.join(', ')}</Card.Text>
                </Card.Body>
            </Card>
        </ListGroup.Item>
    )
}

export default MusicianListItem

