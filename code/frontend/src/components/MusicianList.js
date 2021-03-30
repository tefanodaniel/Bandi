import React from 'react';
import { useSelector, shallowEqual } from "react-redux";
import {Container, Row, Col, Card} from "react-bootstrap";
import {getFrontendURL} from "../utils/api";
import { bandi_styles } from "../styles/bandi_styles";

const selectMusicians = (state) => {
    return state.musician_reducer.filteredMusicians.map(user => user)
}


const chunk = (arr, chunkSize = 1, cache = []) => {
    const tmp = [...arr]
    if (chunkSize <= 0) return cache
    while (tmp.length) cache.push(tmp.splice(0, chunkSize))
    return cache
}

const FilteredMusicianItem = ( musician ) => {
    return (
            <Card style={bandi_styles.musician_card} className="rounded border-0">
                <Card.Body>
                    <Card.Title><b>{musician.name}</b></Card.Title>
                    <Card.Text className="small font-italic" style={{textColor:"white"}}>Genres: {musician.genres.join(', ')}</Card.Text>
                    <Card.Text className="small font-italic" style={{color:"white"}}>Instruments: {musician.instruments.join(', ')}</Card.Text>
                    <Card.Text className="small font-italic"> <a href={getFrontendURL() + "/#/profile?view=" + musician.id} style={{color:"white"}}>View More</a></Card.Text>
                </Card.Body>
            </Card>
    )
}


const MusicianList = () => {
    const fil_musicians = useSelector(selectMusicians, shallowEqual)
    const fil_musicians_chunk = chunk(fil_musicians,4)
    const rows = fil_musicians_chunk.map((user_chunk, index) => {
        const fil_musicians_cols = user_chunk.map((user, index) => {
            return (
                <Col key={index} style={{height: "180px" , columnWidth: "500px"}}>
                    <FilteredMusicianItem id={user.id} name={user.name} instruments={user.instruments}  genres={user.genres}/>
                </Col>
            );
        });
        return <Row key={index} style={{width: "1000px",marginTop:"50px"}}>{fil_musicians_cols}</Row>
    });
    return (
        <Container>
            {rows}
        </Container>
    )
}

export default MusicianList

