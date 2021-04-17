// Miscellaneous Utility functions (that aren't reated to api calls)
import moment from "moment";
import React from "react";

// Genre Related
export const shazam_genre_display_names = () => {
    return ([
        'Pop',
        'Hip-Hop/Rap',
        'Dance',
        'Electronic',
        'R&B/Soul',
        'Alternative',
        'Rock',
        'Latin',
        'Country',
        'AfroBeats',
        'Reggae/Dancehall',
        'House',
        'K-Pop'
    ]);
};

export const shazam_genre_api_names = () => {
    return ([
        'pop',
        'hip-hop-rap',
        'dance',
        'electronic',
        'randb-soul',
        'alternative',
        'rock',
        'latin',
        'country',
        'afrobeats',
        'reggae-dancehall',
        'house',
        'k-pop'
    ]);
};

export function genre_data() {
    const data = [];
    const displaynames = shazam_genre_display_names();
    const apinames = shazam_genre_api_names();
    for(let i = 0; i < 13; i++){
        data.push({
            name: displaynames[i],
            apiname: apinames[i],
        });
    }
    return data;
}

export const createGenreSelectItems = () => {
    let items = [];
    let genres = genre_data();
    for (let i = 0; i < 13; i++) {
        items.push(<option key={i} value={genres[i].apiname}>{genres[i].name}</option>);
    }
    return items;
}


export function convert_genre(genres) {
    let api_genre = genres.join(', ') //only expecting one genre since that's how sotw song posting works.
    const displaynames = shazam_genre_display_names();
    const apinames = shazam_genre_api_names();
    let display_genre = null;
    for(let i = 0; i < 13; i++){
        if(apinames[i] === api_genre) {
            display_genre = displaynames[i];
        }
    }
    return display_genre;
}

// Handy functions
export const chunk = (arr, chunkSize = 1, cache = []) => {
    const tmp = [...arr]
    if (chunkSize <= 0) return cache
    while (tmp.length) cache.push(tmp.splice(0, chunkSize))
    return cache
}

export const delay = ms => new Promise(res => setTimeout(res, ms));

// Time/Date related
export function getWeekDays(weekStart) {
    const days = [weekStart];
    for (let i = 1; i < 7; i += 1) {
        days.push(
            moment(weekStart)
                .add(i, 'days')
                .toDate()
        );
    }
    return days;
}

export function getWeekRange(date) {
    return {
        from: moment(date)
            .startOf('week')
            .toDate(),
        to: moment(date)
            .endOf('week')
            .toDate(),
    };
}

export function getTimeRemaining(endtime) {
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


