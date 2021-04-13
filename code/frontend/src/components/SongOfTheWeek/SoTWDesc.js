import React, {useState} from "react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {Card, Container, Collapse, Row, Col} from "react-bootstrap";
import "../../styles/neon.scss"
import {bandi_styles} from "../../styles/bandi_styles";
import Button from "react-bootstrap/Button";
import DropdownMultiselect from "react-multiselect-dropdown-bootstrap";

// this will be a state selector for the sotw info.
//const selectSongOfTheWeekById = (state, user_id) => {
//    return state.musician_reducer.filteredMusicians.find((user) => user.id === user_id)
//}
const selectSongInfo = (state) => {
    if(!state.sotw_event_reducer.chosen_event_song)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event_song
}

const SoTWDesc = () => {
    //let id; // id should be passed in based on search query
    //const musician = useSelector((state) => selectMusiciansById(state, id))
    const dispatch = useDispatch();
    let song = useSelector(selectSongInfo, shallowEqual);
    const [open, setOpen] = useState(false);

    if(song === -1) {
        return (
            <Card style={bandi_styles.sotw_desc} className="bg-transparent rounded border-0 text-center">
                <section className="light">
                    <Card.Body className="gradient1" fluid>
                        <Card.Text>Loading Event Info ...</Card.Text>
                    </Card.Body>
                </section>
            </Card>
        )
    }


    /**let song = {
        Name: "24K Magic",
        Artist: "Bruno Mars",
        Album: "24K Magic",
        Released: "2016",
        Genres: ["Funk", "Contemporary R&B", "Post-disco"],
        Awards: "Grammy Award for Record of the Year, MORE"
    }*/

    return (
        <Card style={bandi_styles.sotw_desc} className="bg-transparent rounded border-0 text-center">
        <section className="light">
            <Card.Body className="gradient1" fluid>
                <Card.Text><b>Song Of The Week: {song.songName}</b></Card.Text>
                <Card.Text >Artist : {song.artistName}</Card.Text>
                <Card.Text> Year : {song.releaseYear}</Card.Text>
                <Card.Text><p className="small font-italic">Genres: {song.genres.join(', ')}</p></Card.Text>
            </Card.Body>
        </section>
        </Card>
    )
}
export default SoTWDesc
