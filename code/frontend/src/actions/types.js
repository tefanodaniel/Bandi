// Musician API action types
export const LOAD_MUSICIANS_INITIAL = 'musicians/loadfirst';
export const LOAD_MUSICIANS_INITIAL_SEARCH = 'musicians/loadfirstsearch';
export const LOAD_MUSICIANS_QUERY = 'musicians/newquery';
export const CLEAR_MUSICIANS_QUERY = 'musicians/clearquery';

// Band API action types
export const LOAD_BANDS_FOR_MUSICIAN = '/bands/loadmusicianbands';

// User action types
export const USER_LOGIN = 'user/login';
export const USER_LOGOUT = 'user/logout';
export const UPDATE_USER_PROFILE = 'user/updateprofile';

// sd event action types
export const LOAD_SD_EVENTS = '/events/loadsdevents';

// Chat action types
export const CHAT_INITIALIZE = 'chat/initialize';
export const CHAT_LOGIN = 'chat/login';
export const CHAT_LOGOUT = 'chat/logout';

// Song action types
export const ADD_SONG = 'song/add';
export const GET_SONG = '';

// sotw event action types
export const LOAD_SOTW_EVENTS_INITIAL = '/events/load-sotw-events-first';
export const LOAD_SOTW_EVENTS_CURRENT = '/events/load-sotw-event-current'
export const LOAD_SOTW_EVENTS_CURRENT_SONG = '/events/load-sotw-event-current-song'
export const LOAD_SOTW_EVENTS_CURRENT_SUBMISSIONS = '/events/load-sotw-event-current-submissions'
export const CREATE_NEW_USER_SUBMISSION = '/events/submissions/create-new-submission'
export const ADD_SUBMISSION_TO_EVENT = '/events/submissions/add-submission-to-event'
export const LOAD_SOTW_EVENTS_QUERY = 'events/load-sotw-events-query';
export const CLEAR_SOTW_EVENTS_QUERY = 'events/load-sotw-events-query';