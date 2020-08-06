// src/App.js

import React from 'react';
import './App.css';
import Signin from './components/Signin';
import Signout from './components/Signout';
import Signup from './components/Signup';
import Upload from './components/Upload';
import CreateProject from './components/CreateProject';
import ProjectDetail from './components/ProjectDetail';
import { post } from 'axios';
import $, { removeData } from "jquery";
import {} from "jquery.cookie";



class App extends React.Component {
  //props.match.params.projectID
  constructor() {
    super()
    this.state={
      description:'',
      like:0,
      admin:[],
      commitors:[],
      categories:[],
      last_updated: new Date()
    }
  }

  componentDidMount(){
    const url = 'http://localhost:3001/api/project/detail';
    post(url,{projectID:'Muse_Hysteria'})//this.props.match.params.projectID
    .then((response) => {
      var dup_commitors = response.data.commits.map(info => info.artistID);
      var dup_categories = response.data.commits.map(info => info.category);
      const commitors = [...new Set(dup_commitors)] ;
      const categories = [...new Set(dup_categories)];
      this.setState({commitors : commitors});
      this.setState({admin : response.data.admin});
      this.setState({description : response.data.description});
      this.setState({like : response.data.like});
      this.setState({categories : categories});
      this.setState({last_update : new Date(response.data.last_update)})
      console.log(this.state.last_update)
    })
  }

  render() {
    
    // let logged;
    // console.log($.cookie("login_email"))
    // console.log($.cookie("login_userid"))
    // if ($.cookie("login_email")!="null") {
    //   logged = true;
    // } else {
    //   logged = false;
    // }
    // console.log(logged);

    return (
      <div>
        <header className="site-header">
          {/* <h1>{this.props.match.params.projectID}</h1> */}
          <h4>Muse_Hysteria</h4>
          <br/>
          <small>{this.state.description}</small>
          <br/><br/>
          <h5 className="greytext">LAST UPDATED</h5>
          {Date(this.state.last_updated).toString().substring(0,25)}
          <br/><br/>
          <h5 className="greytext">OWNER</h5>
          <span style={{color:"#"}}>{this.state.admin.join()}</span>
          <br/><br/>
          <h5 className="greytext">COMMITORS</h5>
          {this.state.commitors.join()}
          <br/><br/>
          <h5 className="greytext">CATEGORIES</h5>
          {this.state.categories.join()}
          <br/><br/>
          <br/><br/>
          
          <Upload projectID="Muse_Hysteria" userid={$.cookie("login_userid")}/>
          
        </header>
        <div className="site-content">
          {/* {logged? <Signout /> : <div><Signin /><Signup /></div>} */}
          <ProjectDetail projectID="Muse_Hysteria"/>
          
          {/* <CreateProject userid={$.cookie("login_userid")}/> */}
        </div>
      </div>
      
    )
  }
}


export default App;