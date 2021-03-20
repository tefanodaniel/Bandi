import { combineReducers } from 'redux';

import MusicianReducer from './features/browse/MusicianBrowse';

const rootReducer = combineReducers({
    mreducer : MusicianReducer
});

export default rootReducer;