import React, {useEffect} from "react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {Card} from "react-bootstrap";
import "../../styles/neon.scss"
import "../../styles/clock.css"
import {bandi_styles} from "../../styles/bandi_styles";
import moment from "moment";
import {updateClockStateWrapper} from "../../actions/sotw_event_actions";
import {convert_genre, getTimeRemaining} from "../../utils/miscellaneous";
import {selectSotwSongInfo, selectSotwEventDays, selectSotwEventClockState} from "../../selectors/sotw_selector";

const SotwEventDesc = () => {
    let song = useSelector(selectSotwSongInfo, shallowEqual);
    let days = useSelector(selectSotwEventDays, shallowEqual);
    let clock_state = useSelector(selectSotwEventClockState, shallowEqual);
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
                <Card>
                    <Card.Body className="gradient1" fluid="true">
                    <Card.Text>Loading Event Info ...</Card.Text>
                    </Card.Body>
                </Card>
            </div>)
    }

    function runClock(endtime) {
        //console.log("endtime is ", endtime);
        const temp_dict = getTimeRemaining(endtime);
        //console.log("what is my temp dict", temp_dict);
        dispatch(updateClockStateWrapper(temp_dict));
    }

    return (
        <div>
        <Card>
            <Card.Body>
                <Card.Text><b>Song Of The Week: {song.songName}</b></Card.Text>
                <Card.Text >Artist : {song.artistName}</Card.Text>
                <Card.Text className="font-italic">Genre: {convert_genre(song.genres)}</Card.Text>
            </Card.Body>
        </Card>
        {clock_state.total_time_left > 0 && (
        <div>
            <h5>Event ends soon!</h5>
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

export default SotwEventDesc;
