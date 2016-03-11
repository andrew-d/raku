import React from 'react';
import { Route, IndexRoute } from 'react-router';

// Require routes
import About from './pages/About';
import App from './pages/App';
import Home from './pages/Home';
import Tags from './pages/Tags';


const Routes = (
  <Route path='/' component={App}>
    {/* Introduction page */}
    <IndexRoute component={Home} />

    {/* Tags list page */}
    <Route path='/tags' component={Tags} />

    {/* About page */}
    <Route path='/about' component={About} />
  </Route>
);


export default Routes;
