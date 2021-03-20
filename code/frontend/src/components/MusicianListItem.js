import React from "react";
import { useSelector } from "react-redux";
import {Card, ListGroup } from "react-bootstrap";
import {getFrontendURL} from "../utils/api";
import Button from "react-bootstrap/Button";

const selectMusiciansById = (state, user_id) => {
    return state.musician_reducer.filteredMusicians.find((user) => user.id === user_id)
}

const MusicianListItem = ({ id }) => {
    const musician = useSelector((state) => selectMusiciansById(state, id))

    return (
        <div>
        <ListGroup.Item>
            <Card style={{ minwidth: '18rem' , maxwidth: '18rem'}}>
                <Card.Body>
                    <Card.Title>Name: {musician.name}</Card.Title>
                    <Card.Text>Genres: {musician.genres.join(', ')}</Card.Text>
                    <Card.Text>Instruments: {musician.instruments.join(', ')}</Card.Text>
                </Card.Body>
            </Card>
        </ListGroup.Item>

            <a href={getFrontendURL() + "/#/profile?view=" + musician.id}>View More</a>
        </div>
    )
}

export default MusicianListItem

