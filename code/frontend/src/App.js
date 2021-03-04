//import './styles/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, HashRouter, Switch } from 'react-router-dom';

import Discover from './components/Discover';


// <Route path="/discover" exact component={Discover} />


function App() {
  return (
    <HashRouter>

      <Switch>
        <Route component={Discover} />
      </Switch>

    </HashRouter>
  );
}

export default App;
