//import './styles/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, HashRouter, Switch } from 'react-router-dom';

import Signin from './components/Signin';
import Discover from './components/Discover';
import EditProfile from './components/EditProfile';
import MyProfile from './components/MyProfile'
import Band from "./components/Band";

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
        </Switch>
      </HashRouter>
    </div>
  );
}

export default App;
