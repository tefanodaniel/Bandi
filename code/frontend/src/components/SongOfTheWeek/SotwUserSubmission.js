import React, {useState} from "react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {Card, Container, Collapse, Row, Col} from "react-bootstrap";
import "../../styles/neon.scss"
import {bandi_styles} from "../../styles/bandi_styles";
import Button from "react-bootstrap/Button";
import DropdownMultiselect from "react-multiselect-dropdown-bootstrap";
import {getCurrentEventSubmissions, newUserSubmission} from "../../actions/sotw_event_actions";
import {selectSotwSongInfo, selectSotwEventClockState, selectChosenSotwEventId} from "../../selectors/sotw_selector";
import {getLoggedInUser} from "../../selectors/user_selector";
import {delay} from "../../utils/miscellaneous";
import { v4 as uuidv4 } from 'uuid';


const SotwUserSubmission = () => {
    const dispatch = useDispatch();
    let song = useSelector(selectSotwSongInfo, shallowEqual);
    let clock_state = useSelector(selectSotwEventClockState, shallowEqual);
    let logged_user = useSelector(getLoggedInUser, shallowEqual);
    let eventId = useSelector(selectChosenSotwEventId, shallowEqual);

    const [open, setOpen] = useState(false);
    let [instruments, setInstruments] = useState(undefined);
    let [avLink, setAVLink] = useState(undefined);
    let submission_data = {};

    if(song === -1)  {
        return (
            <div class="sotw-user-submission-placeholder">
                <p id="sotw-user-submission-placeholder-text">
                    Here's where you'll see any submissions you've made for the currently
                    selected Song of the Week!
                </p>
            </div>
        )
    }

    if((clock_state.total_time_left<=0) ) {
        return (
            <div>
                <Card id="closed" className="sotw-user-submission-card">
                    <Card.Title>
                        Your Submission
                    </Card.Title>
                    <Card.Body>
                        <Button id="submissions-closed-button" disabled={true} variant="secondary">
                            Sorry submissions are closed!
                        </Button>
                    </Card.Body>
                </Card>
            </div>
        )
    }
    submission_data.musician_id=logged_user.id;
    submission_data.musician_name=logged_user.name;

    const addSubmission = async (submission_data) => {
        //console.log('what am I submitting', submission_data);
        submission_data.instruments = instruments;
        submission_data.avSubmission = avLink;
        console.log("what is my event id :  ", eventId);

        if((eventId !== undefined) && (submission_data.musician_id !== undefined) && (submission_data.instruments !== undefined) && (submission_data.avSubmission !== undefined)) {
            submission_data.submission_id = uuidv4();
            console.log('new user submission??')
            dispatch(newUserSubmission(eventId, submission_data));
            //console.log('have I got eventid', eventId)
            //console.log('in addSubmission')
            //dispatch(getCurrentEventSubmissions(eventId))  // TODO: I think this gets called alredy in newUserSubmission
            await delay(5000);
        }

    }

    return (
        <Card className="sotw-user-submission-card">
            <Card.Title>
                Your Submission
            </Card.Title>
            <Card.Body>
                <Button onClick={() => setOpen(!open)} variant="secondary" aria-controls="collapse-text" aria-expanded={open}>
                    Add your submissions!
                </Button>
                <Collapse in={open}>
                    <div id="collapse-text">
                        <Container fluid>
                            <Row className="justify-content-sm-center">
                                <Col className="col-sm-5">
                                    <h5> Instrument:</h5>
                                </Col>
                                <Col className="col-sm-5">
                                    <DropdownMultiselect
                                        options={["Drums", "Guitar", "Piano", "Keyboard", "Saxophone", "Vocals"]}
                                        name="instruments"
                                        handleOnChange={(instruments) => setInstruments(instruments)}/>
                                </Col>
                            </Row>
                            <Row className="justify-content-sm-center">
                                <Col className="col-sm-5">
                                    <h5> A/V Submission Url:</h5>
                                </Col>
                                <Col className="col-sm-5">
                                    <input onChange={(e) => setAVLink(e.target.value)} style={{width: "120%"}} placeholder='Your submission' type='text'/>
                                </Col>
                            </Row>
                            <Row className="justify-content-sm-left">
                                <Col className="col-sm-5">
                                </Col>
                                <div className="col-sm-7">
                                    <Button variant="primary" onClick={() => addSubmission(submission_data)} >Submit!</Button>
                                </div>
                            </Row>
                        </Container>
                    </div>
                </Collapse>
            </Card.Body>
        </Card>
    )
}
export default SotwUserSubmission
