//import './styles/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, HashRouter, Switch } from 'react-router-dom';

import Signin from './components/Signin';
import Discover from './components/Discover';
import EditProfile from './components/UserProfile/EditProfile';
import MyProfile from './components/UserProfile/MyProfile';
import Band from "./components/Bands/Band";
import CreateBand from "./components/Bands/CreateBand";
import MusicianView from "./components/MusicianSearch/MusicianView";
import BandView from "./components/Bands/BandView";
import Profile from "./components/UserProfile/Profile";
import SongOfTheWeek from "./components/SongOfTheWeek/SongOfTheWeek";
import SpeedDate from "./components/SpeedDating/SpeedDate";
import SpeedDateEvent from "./components/SpeedDating/SpeedDateEvent";
import MyChats from "./components/MyChats";
import SotwUserSubmission from "./components/SongOfTheWeek/SotwUserSubmission";

function App() {
  return (
    <div>
      <HashRouter>
        <Switch>
          <Route exact path="/" component={Discover} />
          <Route exact path="/discover" component={Discover} />
          <Route exact path="/signin" component={Signin} />
          <Route exact path="/editprofile" component={EditProfile} />
          <Route exact path="/myprofile" component={MyProfile} />
          <Route exact path="/band" component={Band} />
          <Route exact path="/createband" component={CreateBand} />
          <Route exact path="/musicianview" component={MusicianView} />
          <Route exact path="/bandview" component={BandView} />
          <Route exact path="/sotw" component={SongOfTheWeek} />
          <Route exact path="/profile" component={Profile} />
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
