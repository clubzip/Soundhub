// src/App.js

import React from 'react';
import './App.css';
import Signin from './components/Signin';
import Signout from './components/Signout';
import Signup from './components/Signup';
import Player from './components/Player';
import Upload from './components/Upload';
import ProjectDetail from './components/ProjectDetail';
import $ from "jquery";
import {} from "jquery.cookie";
import { post, get } from 'axios';



class App extends React.Component {

  render() {
    let logged;
    console.log($.cookie("login_email"))
    if ($.cookie("login_email")!="null") {
      logged = true;
    } else {
      logged = false;
    }
    console.log(logged);

    return (

      <div className="App">
        <Player projectID="testgroup" target="master"/>
        {logged? <Signout /> : <div><Signin /><Signup /></div>}
        <ProjectDetail projectID="testgroup"/>
        <Upload projectID="testgroup" userid={$.cookie("login_userid")}/>
      </div>
    )
  }
}


export default App;