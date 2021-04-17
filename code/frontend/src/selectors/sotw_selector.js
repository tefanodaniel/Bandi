export const selectChosenSongId = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event.songId
}

export const selectChosenSotwEventId = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event.eventId
}

export const checkSotwEvent = (state) => {
    if((state.sotw_event_reducer.chosen_event) && (state.sotw_event_reducer.chosen_event_song))
        return true;
    else
        return false;
}

export const selectSotwSongInfo = (state) => {
    if(!state.sotw_event_reducer.chosen_event_song)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event_song
}

export const selectSotwEventDays = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else
        var temp = {};
    temp.startDay = state.sotw_event_reducer.chosen_event.startDay;
    temp.endDay = state.sotw_event_reducer.chosen_event.endDay;
    return temp;
}

export const selectSotwEventClockState = (state) => {
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

export const selectSotwEventSubmissionIds = (state) => {
    if(!state.sotw_event_reducer.chosen_event)
        return -1;
    else if(!state.sotw_event_reducer.chosen_event.submissions)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event.submissions.map(item => item)
}


export const selectSotwEventSubmissions = (state) => {
    if(!state.sotw_event_reducer.chosen_event_submissions)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event_submissions.map(item => item)
}

