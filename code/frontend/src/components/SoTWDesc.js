import React from "react";
import { useSelector } from "react-redux";
import {Card, ListGroup } from "react-bootstrap";
import {getFrontendURL} from "../utils/api";

const selectMusiciansById = (state, user_id) => {
    return state.musician_reducer.filteredMusicians.find((user) => user.id === user_id)
}

const styles = {
    container: {
        width: 300,
        margin: "0 auto"
    },
    card: {
//        backgroundColor: "#ff8952",
        borderRadius: 35,
        borderStyle: "dashed",
    }
}

const SoTWDesc = () => {
    let id = "22xpmsx47uendfh4kafp3zjmi";
    const musician = useSelector((state) => selectMusiciansById(state, id))

    return (
        <ListGroup.Item style={{ borderStyle: "none" }}>
            <Card style={styles.card} className="rounded shadow-sm border-0">
                <Card.Body fluid>
                    <Card.Title><b>{musician.name}</b></Card.Title>
                    <Card.Text><p className="small text-muted font-italic">Genres: {musician.genres.join(', ')}</p></Card.Text>
                    <Card.Text><p className="small text-muted font-italic">Instruments: {musician.instruments.join(', ')}</p></Card.Text>
                    <Card.Text><p className="small text-muted font-italic"> <a href={getFrontendURL() + "/#/profile?view=" + musician.id}>View More</a></p></Card.Text>
                </Card.Body>
            </Card>
        </ListGroup.Item>
    )
}

export default SoTWDesc

