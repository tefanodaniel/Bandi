import { combineReducers } from 'redux';
import musicianReducer from './musician_reducer';
import userReducer from './user_reducer';

const rootReducer = combineReducers({
    musician_reducer : musicianReducer,
    user_reducer : userReducer
});

export default rootReducer;
