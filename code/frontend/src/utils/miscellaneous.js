import api from "./api";

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

export function convert_genre(genres) {
    let api_genre = genres.join(', ') //only expecting one genre since that's how sotw song posting works.
    const displaynames = shazam_genre_display_names();
    const apinames = shazam_genre_api_names();
    let display_genre = null;
    for(let i = 0; i < 13; i++){
        if(apinames[i] == api_genre) {
            display_genre = displaynames[i];
        }
    }
    return display_genre;
}

