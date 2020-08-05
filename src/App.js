// src/App.js

import React from 'react';
import './App.css';
import Signin from './components/Signin';
import Signout from './components/Signout';
import Signup from './components/Signup';
import Upload from './components/Upload';
import CreateProject from './components/CreateProject';
import ProjectDetail from './components/ProjectDetail';
import $ from "jquery";
import {} from "jquery.cookie";



class App extends React.Component {


  render() {
    let logged;
    console.log($.cookie("login_email"))
    console.log($.cookie("login_userid"))
    if ($.cookie("login_email")!="null") {
      logged = true;
    } else {
      logged = false;
    }
    console.log(logged);

    return (

      <div className="App">
        {logged? <Signout /> : <div><Signin /><Signup /></div>}
        <ProjectDetail projectID="aaaaaaa"/>
        <Upload projectID="aaaaaaa" userid={$.cookie("login_userid")}/>
        <CreateProject userid={$.cookie("login_userid")}/>
      </div>
    )
  }
}


export default App;