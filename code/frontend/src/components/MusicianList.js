import React from 'react';
import { useSelector, shallowEqual } from "react-redux";
import {Container, Row, Col, Card} from "react-bootstrap";
import {getFrontendURL} from "../utils/api";

const selectMusicians = (state) => {
    return state.musician_reducer.filteredMusicians.map(user => user)
}


const chunk = (arr, chunkSize = 1, cache = []) => {
    const tmp = [...arr]
    if (chunkSize <= 0) return cache
    while (tmp.length) cache.push(tmp.splice(0, chunkSize))
    return cache
}


const styles = {
    card: {
        borderRadius: 35,
        borderStyle: "dashed",
        height: "250px"
    }
}

const FilteredMusicianItem = ( musician ) => {
    return (
            <Card style={styles.card} className="rounded shadow-sm border-0">
                <Card.Body fluid>
                    <Card.Title><b>{musician.name}</b></Card.Title>
                    <Card.Text><p className="small text-muted font-italic">Genres: {musician.genres.join(', ')}</p></Card.Text>
                    <Card.Text><p className="small text-muted font-italic">Instruments: {musician.instruments.join(', ')}</p></Card.Text>
                    <Card.Text><p className="small text-muted font-italic"> <a href={getFrontendURL() + "/#/profile?view=" + musician.id}>View More</a></p></Card.Text>
                </Card.Body>
            </Card>
    )
}


const MusicianList = () => {
    const fil_musicians = useSelector(selectMusicians, shallowEqual)
    const fil_musicians_chunk = chunk(fil_musicians,3)
    const rows = fil_musicians_chunk.map((user_chunk, index) => {
        const fil_musicians_cols = user_chunk.map((user, index) => {
            return (
                <Col key={index} style={{height: "250px"}}>
                    <FilteredMusicianItem id={user.id} name={user.name} instruments={user.instruments}  genres={user.genres}/>
                </Col>
            );
        });
        return <Row key={index}>{fil_musicians_cols}</Row>
    });
    return (
        <Container>
            {rows}
        </Container>
    )
}

export default MusicianList

