//import './styles/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, HashRouter, Switch } from 'react-router-dom';

import Signin from './components/Signin';
import Discover from './components/Discover';
import EditProfile from './components/ProfileForm';
import Profile from './components/Profile'
import Band from "./components/Band";
import Callback from "./components/Callback";

function App() {
  return (
    <div>
      <HashRouter>
        <Switch>
          <Route exact path="/callback" component={Callback} />
          <Route exact path="/" component={Discover} />
          <Route exact path="/signin" component={Signin} />
          <Route exact path="/editprofile" component={EditProfile} />
          <Route exact path="/profile" component={Profile} />
          <Route exact path="/band" component={Band} />
        </Switch>
      </HashRouter>
    </div>
  );
}

export default App;
