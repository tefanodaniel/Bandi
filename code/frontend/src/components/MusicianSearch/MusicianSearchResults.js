import React from 'react';
import { useState } from 'react';
import {useSelector, shallowEqual, useDispatch} from "react-redux";
import {Container, Row, Col, Card, Modal, Button } from "react-bootstrap";
import { bandi_styles } from "../../styles/bandi_styles";
import {allMusiciansQuery} from "../../actions/musician_actions";
import FriendApiService from "../../utils/FriendApiService";
import {selectFilteredMusicians} from "../../selectors/musician_selector";
import {chunk} from "../../utils/miscellaneous";
import {getLoggedInUser} from "../../selectors/user_selector";
import {getFrontendURL} from "../../utils/api";

async function addFriend (temp) {
    const response = await FriendApiService.sendFriendRequest(temp.logged_id, temp.id);
    if(response) {
        alert("A request to connect was sent to " + temp.name + ".")
    }
}

const RenderConnectButton = (temp) => {
    // remove question mark once pending_outgoing_requests confirmed to exist
    /*
    if (this.state.pending_outgoing_requests?.indexOf(this.state.userId) == -1) {
        return <Button variant="success" onClick={this.addFriend}>Connect!</Button>
    } else { return <Button disabled>Pending...</Button> };*/
    //if (true) {
        return <Button variant="primary" onClick={() => addFriend(temp)}>Connect!</Button>
    //} else { return <Button disabled>Pending...</Button> };
}

const FilteredMusicianItem = React.forwardRef((musician, ref) => {
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
                                <h5>Links: {musician.links}</h5>
                            </div>

                        </Modal.Body>
                        <Modal.Footer>
                            <Button onClick={() => {window.location = getFrontendURL() + '/#/musiciandetails?view=' + musician.id;}}>
                                View More
                            </Button>
                            <Button variant="secondary" onClick={handleClose}>
                                Close
                            </Button>
                            <RenderConnectButton logged_id = {musician.logged_id} id = {musician.id} name={musician.name}/>
                        </Modal.Footer>
                    </Modal>
                </Card.Body>
            </Card>
    )
});


const MusicianSearchResults = () => {
    const dispatch = useDispatch();
    const fil_musicians = useSelector(selectFilteredMusicians, shallowEqual)
    let logged_user = useSelector(getLoggedInUser, shallowEqual);

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
            if(user.id === logged_user?.id) {
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
                        <FilteredMusicianItem key={index} ref={ref} logged_id = {logged_user?.id} id={user.id} name={user.name} instruments={user.instruments}  genres={user.genres} location={user.location} experience={user.experience} links={user.profileLinks.map((link, i) => <a href={link}>{link}</a>)}/>
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
