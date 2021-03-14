//import './styles/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, HashRouter, Switch } from 'react-router-dom';

import Signin from './components/Signin';
import Discover from './components/Discover';
import CreateProfile from './components/ProfileForm';


function App() {
  return (
    <div>
      <HashRouter>
        <Switch>
          <Route exact path="/" component={Discover} />
          <Route exact path="/signin" component={Signin} />
          <Route exact path="/newprofile" component={CreateProfile} />
        </Switch>
      </HashRouter>
    </div>
  );
}

export default App;
