import { combineReducers } from 'redux';

import MusicianReducer from './features/browse/MusicianBrowse';
import UserReducer from './features/user/UserReducer';

const rootReducer = combineReducers({
    musician_reducer : MusicianReducer,
    user_reducer : UserReducer
});

export default rootReducer;