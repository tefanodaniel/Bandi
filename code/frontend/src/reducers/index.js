import { combineReducers } from 'redux';
import musicianReducer from './musician_reducer';
import userReducer from './user_reducer';
import sdEventReducer from "./sd_event_reducer";
import chatReducer from './chat_reducer';
import sotwEventReducer from "./sotw_event_reducer";
import friendReducer from './friend_reducer';

const rootReducer = combineReducers({
    musician_reducer : musicianReducer,
    friend_reducer: friendReducer,
    user_reducer : userReducer,
    sd_event_reducer : sdEventReducer,
    chat_reducer : chatReducer,
    sotw_event_reducer : sotwEventReducer
});

export default rootReducer;
