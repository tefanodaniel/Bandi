import { combineReducers } from 'redux';
import musicianReducer from './musician_reducer';
import userReducer from './user_reducer';
import sdEventReducer from "./sd_event_reducer";

const rootReducer = combineReducers({
    musician_reducer : musicianReducer,
    user_reducer : userReducer,

    sd_event_reducer : sdEventReducer
});

export default rootReducer;
