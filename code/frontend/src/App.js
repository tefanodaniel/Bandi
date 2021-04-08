//import './styles/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, HashRouter, Switch } from 'react-router-dom';

import Signin from './components/Signin';
import Discover from './components/Discover';
import MyChats from "./components/MyChats";

import EditUserDashboard from './components/UserProfile/EditUserDashboard';
import UserDashboard from './components/UserProfile/UserDashboard';
import MusicianDetails from "./components/UserProfile/MusicianDetails";

import MusicianSearch from "./components/MusicianSearch/MusicianSearch";

import Band from "./components/Bands/Band";
import CreateBand from "./components/Bands/CreateBand";
import BandView from "./components/Bands/BandView";

import SongOfTheWeek from "./components/SongOfTheWeek/SongOfTheWeek";
import SotwUserSubmission from "./components/SongOfTheWeek/SotwUserSubmission";

import SpeedDate from "./components/SpeedDating/SpeedDate";
import SpeedDateEvent from "./components/SpeedDating/SpeedDateEvent";



function App() {
  return (
    <div>
      <HashRouter>
        <Switch>
          <Route exact path="/" component={Discover} />
          <Route exact path="/discover" component={Discover} />
          <Route exact path="/signin" component={Signin} />
          <Route exact path="/edit-user-dashboard" component={EditUserDashboard} />
          <Route exact path="/myprofile" component={UserDashboard} />
          <Route exact path="/band" component={Band} />
          <Route exact path="/createband" component={CreateBand} />
          <Route exact path="/musiciansearch" component={MusicianSearch} />
          <Route exact path="/bandview" component={BandView} />
          <Route exact path="/sotw" component={SongOfTheWeek} />
          <Route exact path="/musiciandetails" component={MusicianDetails} />
          <Route exact path="/speeddate" component={SpeedDate} />
          <Route exact path="/speeddateevent" component={SpeedDateEvent} />
          <Route exact path="/mychats" component={MyChats} />
          <Route exact path="/sotw-user-submission" component={SotwUserSubmission} />
        </Switch>
      </HashRouter>
    </div>
  );
}

export default App;
