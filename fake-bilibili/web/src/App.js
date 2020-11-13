import React from 'react';
import { BrowserRouter, Route} from 'react-router-dom';

import EchartsDemo from './EchartsDemo'
import Demo from './Demo'
import Login from './LoginCard'
import Main from './Main'

class App extends React.Component {
  render = () => {
    return (
      <BrowserRouter>
        <Route path="/login" component={Login}/>
        <Route path="/echarts" component={EchartsDemo}/>
        <Route path="/demo" component={Demo}/>
        <Route path="/av/:name" component={Main}/>
        <Route exact path="/" component={Main}/>
    </BrowserRouter>)
  }
}

export default App;