//import './styles/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, HashRouter, Switch } from 'react-router-dom';

import Signin from './components/Signin';
import Discover from './components/Discover';
import EditProfile from './components/EditProfile';
import MyProfile from './components/MyProfile'
import Band from "./components/Band";
import CreateBand from "./components/CreateBand";
import MusicianView from "./components/MusicianView";
import BandView from "./components/BandView";
import Profile from "./components/Profile";
import SongOfTheWeek from "./components/SongOfTheWeek";
import SpeedDate from "./components/SpeedDate";
import SpeedDateEvent from "./components/SpeedDateEvent";

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
        </Switch>
      </HashRouter>
    </div>
  );
}

export default App;
