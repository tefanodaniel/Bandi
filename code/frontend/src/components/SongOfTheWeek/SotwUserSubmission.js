import React, {useState} from "react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {Card, Container, Collapse, Row, Col} from "react-bootstrap";
import "../../styles/neon.scss"
import {bandi_styles} from "../../styles/bandi_styles";
import Button from "react-bootstrap/Button";
import DropdownMultiselect from "react-multiselect-dropdown-bootstrap";
import {getCurrentEventSubmissions, newUserSubmission} from "../../actions/sotw_event_actions";
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

const selectClockState = (state) => {
    if(!state.sotw_event_reducer.chosen_event_clock) {
        var temp = {}
        temp.total_time_left = -1;
        temp.days_left = -1;
        temp.hours_left = -1;
        temp.minutes_left = -1;
        temp.seconds_left = -1;
        return temp;
    }
    else
        return state.sotw_event_reducer.chosen_event_clock;
}

const selectEventId = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event.eventId
}

const SotwUserSubmission = () => {
    const dispatch = useDispatch();
    let song = useSelector(selectSongInfo, shallowEqual);
    let clock_state = useSelector(selectClockState, shallowEqual);
    let logged_user = useSelector((state) => state.user_reducer, shallowEqual);
    let eventId = useSelector(selectEventId, shallowEqual);
    const [open, setOpen] = useState(false);
    var uuid = require("uuid");
    //let [musician_id, setMusicianId] = useState(undefined);
    let [instruments, setInstruments] = useState(undefined);
    let [avLink, setAVLink] = useState(undefined);
    let submission_data = {};

    if(song === -1)  {
        return (
            <div style={{marginLeft:"-300px"}}>
            </div>
        )
    }

    if((clock_state.total_time_left<=0) ) {
        return (
            <div style={{marginLeft:"-300px"}}>
                <Card style={bandi_styles.sotw_user_submission} className="bg-transparent rounded border-0 text-center">
                    <section className="light">
                        <Card.Body className="gradient1" fluid>
                            <Button disabled={true} variant="secondary">
                                Sorry submissions are closed!
                            </Button>
                        </Card.Body>
                    </section>
                </Card>
            </div>
        )
    }
    submission_data.musician_id=logged_user.id;
    submission_data.musician_name=logged_user.name;
    //setMusicianId(logged_user.id);

    const addInstrumentsToSubmission = (selected_instruments, submission_data) => {
        //setInstruments(selected_instruments);
        submission_data.instruments = selected_instruments;
        console.log("adding these instruments", selected_instruments);
        console.log("submission_data is now ", submission_data);
    }

    const addAVLinkToSubmission = (e, submission_data) => {
        //setAVLink(selected_link);
        console.log(e.target.value);
        submission_data.avSubmission = e.target.value;
        console.log("submission_data is now ", submission_data);
    }

    const delay = ms => new Promise(res => setTimeout(res, ms));

    const addSubmission = async (eventId, submission_data) => {
//        let submission_data = {};
        console.log('what am I submitting', submission_data);
        submission_data.instruments = instruments;
        submission_data.avSubmission = avLink;

        if((submission_data.musician_id !== undefined) && (submission_data.instruments !== undefined) && (submission_data.avSubmission !== undefined)) {
            //alert('Please ensure all parameters are provided');

            submission_data.submission_id = uuid.v4();

            dispatch(newUserSubmission(eventId, submission_data));
            //console.log('have I got eventid', eventId)
            dispatch(getCurrentEventSubmissions(eventId))
            await delay(5000);
        }

    }

    return (
        <Card style={bandi_styles.sotw_user_submission} className="bg-transparent rounded border-0 text-center">
            <section className="light">
                <Card.Body className="gradient1" fluid>
                    <Button onClick={() => setOpen(!open)} variant="secondary" aria-controls="collapse-text" aria-expanded={open}>
                        Add your submissions!
                    </Button>
                    <Collapse in={open}>
                        <div id="collapse-text">
                            <Container fluid>
                                <Row className="justify-content-sm-center" style={{ marginTop:"20px"}}>
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
                                <Row className="justify-content-sm-center" style={{ marginTop:"20px"}}>
                                    <Col className="col-sm-5">
                                        <h5> A/V Submission Url:</h5>
                                    </Col>
                                    <Col className="col-sm-5" style={{minWidth: "65px", textAlign:"center"}}>
                                        <input onChange={(e) => setAVLink(e.target.value)} style={{width: "120%"}} placeholder='Your submission' type='text'/>
                                    </Col>
                                </Row>
                                <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                                    <Col className="col-sm-5">
                                    </Col>
                                    <div className="col-sm-7" style={{textAlign:"center"}}>
                                        <Button variant="primary" onClick={() => addSubmission(eventId, submission_data)} >Submit!</Button>
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


