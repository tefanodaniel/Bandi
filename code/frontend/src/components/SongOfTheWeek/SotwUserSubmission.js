import React, {useState} from "react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {Card, Container, Collapse, Row, Col} from "react-bootstrap";
import "../../styles/neon.scss"
import {bandi_styles} from "../../styles/bandi_styles";
import Button from "react-bootstrap/Button";
import DropdownMultiselect from "react-multiselect-dropdown-bootstrap";
import {clearQuery, newQuery} from "../../actions/musician_actions";

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

const SotwUserSubmission = () => {
    //let id; // id should be passed in based on search query
    //const musician = useSelector((state) => selectMusiciansById(state, id))
    const dispatch = useDispatch();
    let song = useSelector(selectSongInfo, shallowEqual);
    let logged_user = useSelector((state) => state.user_reducer, shallowEqual);
    const [open, setOpen] = useState(false);
    var uuid = require("uuid");
    let submission_data = {}
    submission_data.musician_id = logged_user.id;

    if(song === -1) {
        return (
            <div></div>
        )
    }

    const addInstrumentsToSubmission = (selected_instruments) => {
        submission_data.instruments = selected_instruments;
    }

    const addAVLinkToSubmission = (selected_link) => {
        submission_data.avSubmission = selected_link;
    }

    const addSubmission = () => {
        if(!Object.keys(queryparams)===0) {
            submission_data.submission_id = uuid.v4();
            dispatch(newUserSubmission(submission_data))

        }
    }

    return (
        <Card style={bandi_styles.sotw_desc} className="bg-transparent rounded border-0 text-center">
            <section className="light">
                <Card.Body className="gradient1" fluid>
                    <Button onClick={() => setOpen(!open)} variant="secondary" aria-controls="collapse-text" aria-expanded={open}>
                        Add your submissions!
                    </Button>
                    <Collapse in={open}>
                        <div id="collapse-text">
                            <Container fluid>
                                <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                                    <Col className="col-sm-5">
                                        <h5> Instrument:</h5>
                                    </Col>
                                    <Col className="col-sm-5">
                                        <DropdownMultiselect
                                            options={["Drums", "Guitar", "Piano", "Keyboard", "Saxophone", "Vocals"]}
                                            name="instruments"
                                            handleOnChange={(selected) => {addInstrumentsToSubmission(selected);}}/>
                                    </Col>
                                </Row>
                                <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                                    <Col className="col-sm-5">
                                        <h5> Audio/Video Submission Url:</h5>
                                    </Col>
                                    <div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
                                        <input onChange={link => {addAVLinkToSubmission(link);}} style={{width: "120%"}} placeholder='Your submission' type='text'/>
                                    </div>
                                </Row>
                                <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                                    <Col className="col-sm-5">
                                    </Col>
                                    <div className="col-sm-7" style={{textAlign:"center"}}>
                                        <Button variant="primary" onClick={addSubmission} >Submit!</Button>
                                    </div>
                                </Row>
                            </Container>
                        </div>
                    </Collapse>
                </Card.Body>
            </section>
        </Card>
    )
}
export default SotwUserSubmission


