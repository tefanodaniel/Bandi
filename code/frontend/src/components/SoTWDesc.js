import React from "react";
import { useSelector } from "react-redux";
import {Card, ListGroup } from "react-bootstrap";
import {getFrontendURL} from "../utils/api";
import card_bg from "../images/card.jpg";

const selectMusiciansById = (state, user_id) => {
    return state.musician_reducer.filteredMusicians.find((user) => user.id === user_id)
}

const styles = {
    container: {
        width: 300,
        margin: "0 auto"
    },
    card: {
        borderStyle: "dashed",
        height: "180px",
        marginLeft: "100px",
        backgroundImage: `url(${card_bg})`,
        backgroundPosition: "center",
        backgroundSize:"cover",
        color: "white"
    }
}

const SoTWDesc = () => {
    let id = "22xpmsx47uendfh4kafp3zjmi";
    const musician = useSelector((state) => selectMusiciansById(state, id))
    let song = {
        Name: "24k Magic",
        Artist: "Bruno Mars",
        Album: "24K Magic",
        Released: "2016",
        Genres: ["Funk", "Contemporary R&B", "Post-disco"],
        Awards: "Grammy Award for Record of the Year, MORE"
    }

    return (
        <Card style={styles.card} className="rounded shadow-sm border-0 text-center">
                <Card.Body fluid>
                    <Card.Title className="justify-content-center"><b>{song.Name}</b></Card.Title>
                    <Card.Text><b>{song.Artist}</b></Card.Text>
                    <Card.Text>{song.Album}</Card.Text>
                    <Card.Text><p className="small font-italic">Genres: {song.Genres.join(', ')}</p></Card.Text>
                </Card.Body>
            </Card>
    )
}

export default SoTWDesc

