// File to hold all the state selector functions. To be used as useSelector( fun() , shallowEqual)

// selector for latest musician search query
export const selectPlaceholderQuery = (state) => {
    let query = {};

    if(!state.musician_reducer.query){
        query.name = 'Eric G';
        query.instrument = 'Guitar';
        query.genre = 'Rock';
        query.distance = '50';
        query.experience = 'Expert';
        query.location = 'MD';
        return query;
    }
    else{
        Object.assign(query, state.musician_reducer.query);
        return query
    }
}

export const selectFilteredMusicians = (state) => {
    if(!state.musician_reducer.filteredMusicians)
        return -1;
    else
        return state.musician_reducer.filteredMusicians.map(user => user)
}
