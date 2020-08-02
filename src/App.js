// src/App.js

import React from 'react';
import './App.css';
import Signin from './components/Signin';
import Signout from './components/Signout';
import Signup from './components/Signup';
import Player from './components/Player';
import CommitList from './components/CommitList';
import $ from "jquery";
import {} from "jquery.cookie";
import { post, get } from 'axios';



class App extends React.Component {
  state = {
    information:[]
  }
  componentDidMount(){
    const url = 'http://localhost:3001/api/project/detail';
    post(url,{projectID:"testgroup"})
    .then((response) => {
      console.log("commits")
      console.log(response.data.commits);
      this.setState({information : response.data.commits});
    })
  }

  render() {
    let logged;
    console.log($.cookie("login_email"))
    if ($.cookie("login_email")!="null") {
      logged = true;
    } else {
      logged = false;
    }
    console.log(logged);
    const {information} = this.state;
    return (

      <div className="App">
        <Player projectID="testgroup" target="master"/>
        {logged? <Signout /> : <div><Signin /><Signup /></div>}
        <CommitList projectID="testgroup" data={information}/>
      </div>
    )
  }
}


export default App;