import React from 'react';
import { useState } from 'react';
import {useSelector, shallowEqual, useDispatch} from "react-redux";
import {Container, Row, Col, Card, Modal, Button } from "react-bootstrap";
import { bandi_styles } from "../../styles/bandi_styles";
import {allBandsQuery} from "../../actions/band_actions";
import {selectFilteredBands} from "../../selectors/bands_selector";
import {chunk} from "../../utils/miscellaneous";
import {getLoggedInUser} from "../../selectors/user_selector";

const FilteredBandItem = React.forwardRef((band, ref) => {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
            <Card style={bandi_styles.band_card} className="rounded border-0">
                <Card.Body>
                    <Card.Title><b>{band.name}</b></Card.Title>
                    <Card.Text className="small font-italic" style={{textColor:"white"}}>Genres: {band.genres.join(', ')}</Card.Text>
                    <Card.Text className="small font-italic" style={{color:"white"}}>Members: {band.members.join(', ')}</Card.Text>
                    <Card.Text> <Button variant="primary" style={{width: "170px",
                        height: "30x", marginBottom:"10px"}} onClick={handleShow}>
                        View More
                    </Button>
                    </Card.Text>
                    <Modal key={band.name} show={show} onHide={handleClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>{band.name}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <h5>Location: {band.location}</h5>
                            <div>
                                <h5>members: {band.members.join(", ")}</h5>
                            </div>
                            <div>
                                <h5>Genres: {band.genres.join(", ")}</h5>
                            </div>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleClose}>
                                Close
                            </Button>
                            <RenderConnectButton logged_id = {band.logged_id} id = {band.id} name={band.name}/>
                        </Modal.Footer>
                    </Modal>
                </Card.Body>
            </Card>
    )
});


const BandSearchResults = () => {
    const dispatch = useDispatch();
    const fil_band = useSelector(selectFilteredBands, shallowEqual)
    let logged_user = useSelector(getLoggedInUser, shallowEqual);

    if(fil_bands === -1)
    {
        let queryparams = {};
        queryparams.id = logged_user.id
        dispatch(allBandsQuery(queryparams))

        return (
            <Container>
                <h5 style={{marginTop:"50px", marginLeft:"50px"}}> Loading some of our featured bands!</h5>
            </Container>
        )
    }
    else {
        let fil_bands_mod =JSON.parse(JSON.stringify(fil_bands));
        let index = null;
        index = fil_bands_mod.findIndex(user => {
            if(user.id === logged_user?.id) {
                return user
            }
        });
        if ((index !== null) && index !== -1) fil_bands_mod.splice(index, 1);
        const fil_bands_chunk = chunk(fil_bands_mod,3)
        const rows = fil_bands_chunk.map((user_chunk, index) => {
            const fil_bands_cols = user_chunk.map((user, index) => {
                const ref = React.createRef();
                return (
                    <Col key={index} style={{height: "230px" , columnWidth: "500px"}}>
                        <FilteredBandItem key={index} ref={ref} logged_id = {logged_user?.id} id={user.id} name={user.name} members={user.members}  genres={user.genres} location={user.location} capacity={user.capacity} />
                    </Col>
                );
            });
            return <Row key={index} style={{width: "1000px",marginTop:"50px"}}>{fil_bands_cols}</Row>
        });
        return (
            <Container key="bandsearchresults">
                {rows}
            </Container>
        )
    }
}

export default bandSearchResults;