import React, {useState} from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import {Button, Form} from "react-bootstrap";
import {
    findSotwEventQueryWrapper,
    getCurrentEventSubmissions
} from "../../actions/sotw_event_actions";
import Helmet from 'react-helmet';
import moment from 'moment';
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';
import {delay, getWeekDays, getWeekRange, createGenreSelectItems} from "../../utils/miscellaneous";
import {checkSotwEvent, selectChosenSongId, selectChosenSotwEventId} from "../../selectors/sotw_selector";

const SotwSearchControls = () => {
    const dispatch = useDispatch();
    let chosen_eventId = useSelector(selectChosenSotwEventId, shallowEqual);
    //let chosen_songId = useSelector(selectChosenSongId, shallowEqual);
    //let checkEventFlag = useSelector(checkSotwEvent, shallowEqual);
    let eventparams = {};

    const [genre, setGenre] = useState(undefined);
    const [selectedDays, setSelectedDays] = useState(undefined);
    const [hoverRange, setHoverRange] = useState([]);

    const handleDayChange = date => {
        setSelectedDays(getWeekDays(getWeekRange(date).from))
    };

    const handleDayEnter = date => {
        setHoverRange(getWeekRange(date))
    };

    const handleDayLeave = () => {
        setHoverRange(undefined)
    };

    const handleWeekClick = (weekNumber, days, e) => {
        setSelectedDays(days)
    };

    let daysAreSelected;
    if(selectedDays!== undefined && selectedDays.length) {
        daysAreSelected = false;
    }
    else {
        daysAreSelected = true;
    }

    let range;
    if(daysAreSelected && selectedDays!== undefined && (selectedDays.length === 7)) {
        range = {
            from: selectedDays[0],
            to: selectedDays[6]
        }
    }

    const addgenrequery = (e) => {
        let genre = e.target.value;
        setGenre(genre)
    }

    const SubmitEventQuery = async () => {
        eventparams.genre = genre
        eventparams.startday = moment(selectedDays[0]).format('LL')
        eventparams.endday = moment(selectedDays[6]).format('LL')
        //console.log('Eventparams', eventparams);
        dispatch(findSotwEventQueryWrapper(eventparams));
        await delay(5000);
        dispatch(getCurrentEventSubmissions(chosen_eventId))
        await delay(5000);
        //console.log(checkEventFlag)
    }

    const modifiers = {
        hoverRange: hoverRange,
        selectedRange: range,
        hoverRangeStart: hoverRange && hoverRange.from,
        hoverRangeEnd: hoverRange && hoverRange.to,
        selectedRangeStart: daysAreSelected && selectedDays!== undefined && selectedDays[0],
        selectedRangeEnd: daysAreSelected && selectedDays!== undefined && (selectedDays.length === 7) && selectedDays[6],
    };

    return (
        <Container>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-4" style={{marginTop:"20px"}}>
                    <b>Pick a Week:</b>
                </Col>
                <Col className="col-sm-7">
                <div className="CalendarWeekPicker">
                <DayPicker
                    selectedDays={selectedDays}
                    showWeekNumbers
                    showOutsideDays
                    modifiers={modifiers}
                    onDayClick={handleDayChange}
                    onDayMouseEnter={handleDayEnter}
                    onDayMouseLeave={handleDayLeave}
                    onWeekClick={handleWeekClick}
                />
                <Helmet>
                    <style>{`
                    .CalendarWeekPicker .DayPicker-Month {
                        border-collapse: separate;
                    }
                    .CalendarWeekPicker .DayPicker-WeekNumber {
                        outline: none;
                    }
                    .CalendarWeekPicker .DayPicker-Day {
                        outline: none;
                        border: 1px solid transparent;
                    }
                    .CalendarWeekPicker .DayPicker-Day--hoverRange {
                        background-color: #EFEFEF !important;
                    }

                    .CalendarWeekPicker .DayPicker-Day--selectedRange {
                        background-color: #fff7ba !important;
                        border-top-color: #FFEB3B;
                        border-bottom-color: #FFEB3B;
                        border-left-color: #fff7ba;
                        border-right-color: #fff7ba;
                    }

                    .CalendarWeekPicker .DayPicker-Day--selectedRangeStart {
                        background-color: #FFEB3B !important;
                        border-left: 1px solid #FFEB3B;
                    }

                    .CalendarWeekPicker .DayPicker-Day--selectedRangeEnd {
                        background-color: #FFEB3B !important;
                        border-right: 1px solid #FFEB3B;
                    }

                    .CalendarWeekPicker .DayPicker-Day--selectedRange:not(.DayPicker-Day--outside).DayPicker-Day--selected,
                    .CalendarWeekPicker .DayPicker-Day--hoverRange:not(.DayPicker-Day--outside).DayPicker-Day--selected {
                        border-radius: 0 !important;
                        color: black !important;
                    }
                    .CalendarWeekPicker .DayPicker-Day--hoverRange:hover {
                        border-radius: 0 !important;
                    }
                    `}</style>
                </Helmet>
                </div>
                </Col>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-5">
                    <b>Genre:</b>
                </Col>
                <Form className="col-sm-7" style={{textAlign:"center"}}>
                    <Form.Group controlId="exampleForm.SelectCustom">
                        <Form.Control as="select" onChange={addgenrequery} custom>
                            {createGenreSelectItems()}
                        </Form.Control>
                    </Form.Group>
                </Form>
            </Row>
            <Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
                <Col className="col-sm-5">
                </Col>
                <div className="col-sm-7" style={{textAlign:"center"}}>
                    <Button variant="primary" onClick={SubmitEventQuery} >See Event!</Button>
                </div>
            </Row>


        </Container>
    )
}

export default SotwSearchControls
