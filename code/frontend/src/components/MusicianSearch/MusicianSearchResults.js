import React from 'react';
import { useState } from 'react';
import {useSelector, shallowEqual, useDispatch} from "react-redux";
import {Container, Row, Col, Card, Modal, Button } from "react-bootstrap";
import {getFrontendURL} from "../../utils/api";
import { bandi_styles } from "../../styles/bandi_styles";
import {allMusiciansQuery} from "../../actions/musician_actions";
import { sendFriendRequest } from "../../actions/friend_actions";
import RequestApiService from "../../utils/RequestApiService";
import SotwEventsApi from "../../utils/SotwEventsApiService";

const selectMusicians = (state) => {
    if(!state.musician_reducer.filteredMusicians)
        return -1;
    else
        return state.musician_reducer.filteredMusicians.map(user => user)
}


const chunk = (arr, chunkSize = 1, cache = []) => {
    const tmp = [...arr]
    if (chunkSize <= 0) return cache
    while (tmp.length) cache.push(tmp.splice(0, chunkSize))
    return cache
}

async function handleConnect(senderID, recipientID, recipientName, dispatch) {
    if (senderID && recipientID && recipientName) {
        const response = dispatch(sendFriendRequest(senderID, recipientID));
        if(response) {
            alert("Friend request sent to " + recipientName + "!")
        }
    } else {
        alert("Unable to send friend request.")
    }
}

const RenderConnectButton = (props) => {
    const senderID = props.logged_id;
    const recipientID = props.id;
    const recipientName = props.name;
    const dispatch = props.dispatch;
    
    let friend_reducer = useSelector((state) => state.friend_reducer, shallowEqual);
    let outgoing = friend_reducer.outgoing_friend_requests;
    // Check if logged in user has already sent a friend request to this user
    let already_requested = false;
    outgoing.forEach((req) => {
        if (req.recipientID === recipientID) {
            already_requested = true;
        }
    })

    if (!already_requested) {
        return <Button variant="primary" onClick={() => handleConnect(senderID, recipientID, recipientName, dispatch)}>Connect!</Button>
    } else { return <Button disabled>Pending...</Button> };
}

const FilteredMusicianItem = React.forwardRef((props, ref) => {
    const logged_id = props.logged_id;
    const musician = props.musician;
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    //<Card.Text className="small font-italic"> <a href={getFrontendURL() + "/#/musiciandetails?view=" + musician.id} style={{color:"white"}}>View More</a></Card.Text>
    //
    return (
            <Card style={bandi_styles.musician_card} className="rounded border-0">
                <Card.Body>
                    <Card.Title><b>{musician.name}</b></Card.Title>
                    <Card.Text className="small font-italic" style={{textColor:"white"}}>Genres: {musician.genres.join(', ')}</Card.Text>
                    <Card.Text className="small font-italic" style={{color:"white"}}>Instruments: {musician.instruments.join(', ')}</Card.Text>
                    <Card.Text> <Button variant="primary" style={{width: "170px",
                        height: "30x", marginBottom:"10px"}} onClick={handleShow}>
                        View More
                    </Button>
                    </Card.Text>
                    <Modal key={musician.name} show={show} onHide={handleClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>{musician.name}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <h5>Location: {musician.location}</h5>
                            <h5>Experience: {musician.experience}</h5>
                            <div>
                                <h5>Instruments: {musician.instruments.join(", ")}</h5>
                            </div>
                            <div>
                                <h5>Genres: {musician.genres.join(", ")}</h5>
                            </div>
                            <div>
                                <h5>Links: {musician.profileLinks.map((link, i) => <a key={i} href={link}>{link}</a>)}</h5>
                            </div>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleClose}>
                                Close
                            </Button>
                            <RenderConnectButton logged_id = {logged_id} id = {musician.id} name={musician.name} dispatch={props.dispatch}/>
                        </Modal.Footer>
                    </Modal>
                </Card.Body>
            </Card>
    )
});


const MusicianSearchResults = () => {
    const dispatch = useDispatch();
    const fil_musicians = useSelector(selectMusicians, shallowEqual)
    let logged_user = useSelector((state) => state.user_reducer, shallowEqual);
    let logged_user_friend_reducer = useSelector((state) => state.friend_reducer, shallowEqual);
    

    if(fil_musicians === -1)
    {
        let queryparams = {};
        queryparams.id = logged_user.id
        dispatch(allMusiciansQuery(queryparams))

        return (
            <Container>
                <h5 style={{marginTop:"50px", marginLeft:"50px"}}> Loading some of our featured musicians!</h5>
            </Container>
        )
    }
    else {
        let fil_musicians_mod =JSON.parse(JSON.stringify(fil_musicians));
        let index = null;
        index = fil_musicians_mod.findIndex(user => {
            if(user.id === logged_user.id) {
                return user
            }
        });
        if ((index !== null) && index !== -1) fil_musicians_mod.splice(index, 1);
        const fil_musicians_chunk = chunk(fil_musicians_mod,3)
        const rows = fil_musicians_chunk.map((user_chunk, index) => {
            const fil_musicians_cols = user_chunk.map((user, index) => {
                const ref = React.createRef();
                return (
                    <Col key={index} style={{height: "230px" , columnWidth: "500px"}}>
                        <FilteredMusicianItem key={index} ref={ref} logged_id = {logged_user.id} musician={user} dispatch={dispatch} />
                    </Col>
                );
            });
            return <Row key={index} style={{width: "1000px",marginTop:"50px"}}>{fil_musicians_cols}</Row>
        });
        return (
            <Container key="musiciansearchresults">
                {rows}
            </Container>
        )
    }
}


export default MusicianSearchResults;
