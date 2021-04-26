// File to hold all the state selector functions. To be used as useSelector( fun() , shallowEqual)

// selector for latest musician search query
export const selectPlaceholderQuery = (state) => {
    let query = {};

    if(!state.musician_reducer.query){
        query.name = 'Search by Name';
        query.instrument = 'Search by Instrument';
        query.genre = 'Search by Genre';
        query.distance = 'Search by Distance';
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
