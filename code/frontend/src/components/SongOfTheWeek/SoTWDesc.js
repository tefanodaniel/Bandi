import React, {useEffect} from "react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {Card} from "react-bootstrap";
import "../../styles/neon.scss"
import "../../styles/clock.css"
import {bandi_styles} from "../../styles/bandi_styles";
import moment from "moment";
import {updateClockStateWrapper} from "../../actions/sotw_event_actions";

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

const selectEventDays = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        var temp = {};
        temp.startDay = state.sotw_event_reducer.chosen_event.startDay;
        temp.endDay = state.sotw_event_reducer.chosen_event.endDay;
        return temp;
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


const SoTWDesc = () => {
    let song = useSelector(selectSongInfo, shallowEqual);
    let days = useSelector(selectEventDays, shallowEqual);
    let clock_state = useSelector(selectClockState, shallowEqual);
    const dispatch = useDispatch();
    useEffect(() => {
        const timer = setTimeout(
            () => {
                //console.log(days.endDay)
                if(days.endDay) {
                    //console.log("about to run clock state");
                    runClock(moment(days.endDay).format('LL'))
                }
            },
            1000
        );
        return () => clearTimeout(timer);
    });

    if((days === -1) || (song === -1)) {
        return (
            <div>
            <Card style={bandi_styles.sotw_desc} className="bg-transparent rounded border-0 text-center">
                <section className="light">
                    <Card.Body className="gradient1" fluid="true">
                        <Card.Text>Loading Event Info ...</Card.Text>
                    </Card.Body>
                </section>
            </Card>
            </div>)
    }

    function getTimeRemaining(endtime) {
        const total = Date.parse(endtime)  + 24 * 60 * 60 * 1000 - Date.parse(new Date().toLocaleString("en-US", {timeZone: "America/New_York"})
        );
        const seconds = Math.floor((total / 1000) % 60);
        const minutes = Math.floor((total / 1000 / 60) % 60);
        const hours = Math.floor((total / (1000 * 60 * 60)) % 24);
        const days = Math.floor(total / (1000 * 60 * 60 * 24));
        return {
            total_time_left: total,
            days_left: days,
            hours_left: hours,
            minutes_left: minutes,
            seconds_left: seconds
        };
    }

    function runClock(endtime) {
        //console.log("endtime is ", endtime);
        const temp_dict = getTimeRemaining(endtime);
        //console.log("what is my temp dict", temp_dict);
        dispatch(updateClockStateWrapper(temp_dict));
    }


    //if(days.endDay !== undefined){
        //timeleft = getTimeRemaining(moment(days.endDay).format('LL'))
        //console.log("TIME LEFT IS ", timeleft);
    //console.log("about to run clock state");
    //runClock(moment(days.endDay).format('LL'))
    //}


    return (
        <div>
        <Card style={bandi_styles.sotw_desc} className="bg-transparent rounded border-0 text-center">
        <section className="light">
            <Card.Body className="gradient1" fluid="true" style={{marginBottom:"40px"}}>
                <Card.Text><b>Song Of The Week: {song.songName}</b></Card.Text>
                <Card.Text >Artist : {song.artistName}</Card.Text>
                <Card.Text> Year : {song.releaseYear}</Card.Text>
                <Card.Text className="small font-italic">Genres: {song.genres.join(', ')}</Card.Text>
            </Card.Body>
        </section>
        </Card>
        {clock_state.total_time_left > 0 && (
        <div className="col-sm-12 justify-content-md-center" style={{marginTop:"100px", marginLeft:"200px", marginBottom:"30px"}}>
            <h5 style={{marginLeft:"80px"}}>Event ends soon!</h5>
            <div id="clockdiv">
                        <div>
                            <span className="days">{clock_state.days_left}</span>
                            <div className="smalltext">Days</div>
                        </div>
                        <div>
                            <span className="hours">{clock_state.hours_left}</span>
                            <div className="smalltext">Hours</div>
                        </div>
                        <div>
                            <span className="minutes">{clock_state.minutes_left}</span>
                            <div className="smalltext">Minutes</div>
                        </div>
                        <div>
                            <span className="seconds">{clock_state.seconds_left}</span>
                            <div className="smalltext">Seconds</div>
                        </div>
                    </div>
            </div>)}
        </div>
            )};

export default SoTWDesc;
