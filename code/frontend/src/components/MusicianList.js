import React from 'react';
import { useSelector, shallowEqual } from "react-redux";
import MusicianListItem from './MusicianListItem'
import {Container, ListGroup} from "react-bootstrap";

const selectMusicianIds = (state) => {
    return state.mreducer.filteredMusicians.map(user => user.id)
}

const MusicianList = () => {
    const musicianIds = useSelector(selectMusicianIds, shallowEqual)
    const renderedListItems = musicianIds.map( (user_id) => {
        return <MusicianListItem key = {user_id} id = {user_id} />
    })
    return <Container style={{display:'flex'}}><ListGroup>{renderedListItems}</ListGroup></Container>
}

export default MusicianList

