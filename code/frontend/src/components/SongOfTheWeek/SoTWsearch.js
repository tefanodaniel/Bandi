import React, {useState} from 'react';
import {shallowEqual, useDispatch, useSelector} from 'react-redux';
import {Container, Row, Col} from "react-bootstrap";
import {Button, Form} from "react-bootstrap";
import {
    findSotwEventQueryWrapper,
    getCurrentEventSong,
    getCurrentEventSubmissions
} from "../../actions/sotw_event_actions";
import {genre_data} from "../../utils/miscellaneous";
//import shazamChartApi from "../../utils/ShazamChartApiService";
//import * as Papa from 'papaparse';
//import $ from 'jquery';
//import DatePicker from "react-datepicker";
import Helmet from 'react-helmet';
import moment from 'moment';
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';

//import "react-datepicker/dist/react-datepicker.css";

//var startOfWeek = require('date-fns/startOfWeek');
//var endOfWeek = require('date-fns/endOfWeek');
//var parseCSV = require('papa/parse');

const selectSongId = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event.songId
}

const selectEventId = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event.eventId
}

const checkEvent = (state) => {
    if((state.sotw_event_reducer.chosen_event) && (state.sotw_event_reducer.chosen_event_song))
        return true;
    else
        return false;
}


function getWeekDays(weekStart) {
    //console.log("inside getWeekDays")
    const days = [weekStart];
    for (let i = 1; i < 7; i += 1) {
        days.push(
            moment(weekStart)
                .add(i, 'days')
                .toDate()
        );
    }
    //console.log("returning days : ", days)
    return days;
}

function getWeekRange(date) {
    //console.log("inside getWeekRange")
    return {
        from: moment(date)
            .startOf('week')
            .toDate(),
        to: moment(date)
            .endOf('week')
            .toDate(),
    };
}

async function testchart() {
    //let genre = 'afrobeats';
    //const response = await shazamChartApi.getGenre(genre);
    //let data = Papa.parse(response);
    //console.log(data);
    //var name = "afrobeats";
    //var url = "http://anyorigin.com/go?url=" + encodeURIComponent("https://www.shazam.com/services/charts/csv/genre/world/") + name + "&callback=?";
    //$.get(url, function(response) {  console.log('RESPONSE IS : ',response);});
}


const SoTWsearch = () => {
    const dispatch = useDispatch();
    let chosen_eventId = useSelector(selectEventId);
    let chosen_songId = useSelector(selectSongId);
    let checkEventFlag = useSelector(checkEvent);
    let eventparams = {};

    const createGenreSelectItems = () => {
        let items = [];
        let genres = genre_data();
        for (let i = 0; i < 13; i++) {
            items.push(<option key={i} value={genres[i].apiname}>{genres[i].name}</option>);
        }
        return items;
    }
    //testchart();

    const [genre, setGenre] = useState(undefined);
    const [selectedDays, setSelectedDays] = useState(undefined);
    const [hoverRange, setHoverRange] = useState([]);

    const handleDayChange = date => {
        //console.log("inside handleDayChange")
        setSelectedDays(getWeekDays(getWeekRange(date).from))
        //state.selectedDays= getWeekDays(getWeekRange(date).from)
        //console.log("updated selected Days : ", selectedDays)
    };

    const handleDayEnter = date => {
        //console.log("inside handleDayEnter")
        setHoverRange(getWeekRange(date))
        //state.hoverRange= getWeekRange(date)
        //console.log("updated hoverRange : ", hoverRange)
    };

    const handleDayLeave = () => {
        //console.log("inside handleDayLeave")
        setHoverRange(undefined)
        //state.hoverRange= undefined
        //console.log("updated hoverRange : ", hoverRange)
    };

    const handleWeekClick = (weekNumber, days, e) => {
        //console.log("inside handleWeekClick")
        setSelectedDays(days)
        //state.selectedDays= days
        //console.log("updated selected Days : ", selectedDays)
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
        //console.log("event params should now has genre ", genre);
    }

    const delay = ms => new Promise(res => setTimeout(res, ms));

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

export default SoTWsearch
/*
The search functionality for Song of the week hasn't been implemented yet.
<Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
<Col className="col-sm-3">
    <h5> Instrument:</h5>
</Col>
<div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
<input onChange={e => {addinstrumentquery(e);}} style={{width: "120%"}} placeholder='Search by Instrument' type='text'/>
</div>
</Row>
<Row className="justify-content-sm-left" style={{ marginTop:"20px"}} >
<Col className="col-sm-3">
<h5> Genre :</h5>
</Col>
<div className="col-sm-7" style={{minWidth: "175px", textAlign:"center"}}>
<input onChange={e => {addgenrequery(e);}} style={{width: "120%"}} placeholder='Search by Genre' type='text'/>
</div>
</Row>
<Row className="justify-content-sm-left" style={{ marginTop:"20px"}}>
<Col className="col-sm-5">
</Col>
<div className="col-sm-7" style={{textAlign:"center"}}>
<Button variant="primary" onClick={SubmitQuery} >Submit!</Button>
</div>
</Row>




                {selectedDays!== undefined && (selectedDays.length === 7) && (
                    <div>
                        {'Sunday ' + moment(selectedDays[0]).format('LL')} –{' '}
                        {'Saturday '+ moment(selectedDays[6]).format('LL')}
                    </div>
                )}

*/
