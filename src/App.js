// src/App.js

import React from 'react';
import './App.css';
import Signin from './components/Signin';
import Signout from './components/Signout';
import Signup from './components/Signup';
import Player from './components/Player';
import Upload from './components/Upload';
import CommitList from './components/CommitList';
import $ from "jquery";
import {} from "jquery.cookie";
import { post, get } from 'axios';



class App extends React.Component {
  state = {
    commitlist:[],
    requestlist:[],
    adminlist:[]
  }
  componentDidMount(){
    const url = 'http://localhost:3001/api/project/detail';
    post(url,{projectID:"testgroup"})
    .then((response) => {
      console.log("commits")
      console.log(response.data.commits);
      this.setState({commitlist : response.data.commits});
      this.setState({requestlist : response.data.requests});
      this.setState({adminlist : response.data.admin});
      
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
    const {commitlist, requestlist, adminlist} = this.state;
  
    return (

      <div className="App">
        <Player projectID="testgroup" target="master"/>
        {logged? <Signout /> : <div><Signin /><Signup /></div>}
        <CommitList projectID="testgroup" data={commitlist}/>
        {/* {adminlist.includes($.cookie("login_userid"))
        ?
        <RquestList projectID="testgroup" data={requestlist}/>
        :
        null
        } */}
        <Upload projectID="testgroup" userid={$.cookie("login_userid")}/>
      </div>
    )
  }
}


export default App;