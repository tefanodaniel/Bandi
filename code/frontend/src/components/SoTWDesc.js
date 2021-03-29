import React from "react";
import { useSelector } from "react-redux";
import {Card} from "react-bootstrap";
import "../styles/neon.scss"
import {bandi_styles} from "../styles/bandi_styles";

// this will be a state selector for the sotw info.
//const selectSongOfTheWeekById = (state, user_id) => {
//    return state.musician_reducer.filteredMusicians.find((user) => user.id === user_id)
//}

const SoTWDesc = () => {
    //let id; // id should be passed in based on search query
    //const musician = useSelector((state) => selectMusiciansById(state, id))
    let song = {
        Name: "24K Magic",
        Artist: "Bruno Mars",
        Album: "24K Magic",
        Released: "2016",
        Genres: ["Funk", "Contemporary R&B", "Post-disco"],
        Awards: "Grammy Award for Record of the Year, MORE"
    }

    return (
        <Card style={bandi_styles.sotw_desc} className="bg-transparent rounded border-0 text-center">
        <section className="light">
            <Card.Body className="gradient1" fluid>
                <Card.Text><b>Song Of The Week: {song.Name}</b></Card.Text>
                <Card.Text >Artist : {song.Artist}</Card.Text>
                <Card.Text>Album : {song.Album}</Card.Text>
                <Card.Text><p className="small font-italic">Genres: {song.Genres.join(', ')}</p></Card.Text>
            </Card.Body>
        </section>
        </Card>
    )
}
export default SoTWDesc

