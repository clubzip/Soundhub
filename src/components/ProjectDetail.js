import React, { Component } from 'react';
import Commit from './Commit';
import AudioPlayer from 'react-h5-audio-player';
import 'react-h5-audio-player/lib/styles.css';
import $ from "jquery";
import {} from "jquery.cookie";
import { post } from 'axios';

class ProjectDetail extends Component {
  //projectID를 props로 받아야 함.
  state = {
    play:false,
    commitlist:[],
    requestlist:[],
    adminlist:[],
    clickedlist:[],
    request_clickedlist:[],
    url: "/"+this.props.projectID+"/master.mp3",
    refsCollection : {}
  }
  

  componentDidMount(){
    const url = 'http://localhost:3001/api/project/detail';
    post(url,{projectID:this.props.projectID})
    .then((response) => {
      console.log("commits")
      console.log(response.data.commits);
      this.setState({commitlist : response.data.commits});
      this.setState({requestlist : response.data.requests});
      this.setState({adminlist : response.data.admin});
    })
    console.log(this.state.refsCollection)
    console.log(this.state.clickedlist)
    console.log(this.state.requestlist)
  }

  commitItemChecked = (commitID) => {
    
    console.log(commitID)
    //일단 this.state.play가 true면 pause 실행
    if(this.state.play){
      this.pause();
    }
    //clickedlist에 commitID 추가
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.concat(commitID)}, ()=>{console.log(this.state.clickedlist)})
    
    
  }
  commitItemUnchecked = (commitID) => {
    
    //일단 this.state.play가 true면 pause 실행
    if(this.state.play){
      this.pause();
    }
    //clickedlist에 commitID 제거
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.filter(info=> info !== commitID)}, ()=>{console.log(this.state.clickedlist)})
    console.log(this.state.clickedlist)
  }
  requestItemChecked = (commitID) => {
    //일단 this.state.play가 true면 pause 실행
    if(this.state.play){
      this.pause();
    }
    //clickedlist에 commitID 추가
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.concat(commitID)}, ()=>{console.log(this.state.clickedlist)})
    //requestlist에 commitID 추가
    const origin_request_clickedlist = this.state.request_clickedlist;
    this.setState({request_clickedlist: origin_request_clickedlist.concat(commitID)}, ()=>{console.log(this.state.request_clickedlist)})
  }
  requestItemUnchecked = (commitID) => {
    //일단 this.state.play가 true면 pause 실행
    if(this.state.play){
      this.pause();
    }
    //clickedlist에 commitID 제거
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.filter(info=> info !== commitID)}, ()=>{console.log(this.state.clickedlist)})
    //requestlist에 commitID 제거
    const origin_request_clickedlist = this.state.request_clickedlist;
    this.setState({request_clickedlist: origin_request_clickedlist.filter(info=> info !== commitID)}, ()=>{console.log(this.state.request_clickedlist)})
  }

  acceptClicked = (e) => {
    // request_clickedlist에 있는 녀석들을 body에 포함해서 request를 보냄.
  }

  rejectClicked = (e) => {
    // request_clickedlist에 있는 녀석들을 body에 포함해서 request를 보냄.
  }

  playClicked = (e) => {
    //clickedlist에 있는 녀석들(i)을 this.refsCollection[i].player.current.audio.play()
    for(var commitID of this.state.clickedlist){
      console.log(commitID)
      this.state.refsCollection[commitID].player.current.audio.current.play()
    }
    this.setState({play:true},()=>{console.log(this.state.play)})
    //this.state.play = true
  }
  
  pause=()=>{
    // for(var commitID of this.state.clickedlist){
    //   console.log(commitID)
    //   this.state.refsCollection[commitID].player.current.audio.current.play()
    // }
    //clickedlist에 있는 녀석들(i)을 this.refsCollection[i].player.current.audio.pause()
    //this.state.play = false
  }

  pauseClicked = (e) => {
    //pause 실행
  }
  
  render() {
    const commitlist = this.state.commitlist;
    const requestlist = this.state.requestlist;
    
    const CommitList = commitlist.map(
      info => (
        <Commit
          onCheck={this.commitItemChecked}
          onUncheck={this.commitItemUnchecked}
          ref={(instance)=>{this.state.refsCollection[info.commitID] = instance}}
          commitID={info.commitID}
          projectID={this.props.projectID}
          artistID={info.artistID}
          category={info.category}
        />)
    );
    const RequestList = requestlist.map(
      info => (
        <Commit
          onCheck={this.requestItemChecked}
          onUncheck={this.requestItemUnchecked}
          ref={(instance)=>{this.state.refsCollection[info.commitID] = instance}}
          commitID={info.commitID}
          projectID={this.props.projectID}
          artistID={info.artistID}
          category={info.category}
        />)
    );

    return (
      <div>
        {/* Master player */}
        <div>
          <AudioPlayer
              layout="horizontal"
              src={this.state.url}
              onPlay={e => console.log("onPlay")}
              // other props here
              />
        </div>

        {/* Commit list */}
        <div>
          {CommitList}
        </div>
        
        {/* Request list */}
        {
        this.state.adminlist.includes($.cookie("login_userid"))
        ?
        <div>{RequestList}</div>
        :
        null
        }
        
        {/* Master manger */}
        <div>
          <button onClick={this.playClicked}>play</button>
          <button onClick={this.pauseClicked}>pause</button>
          <button onClick={this.acceptClicked}>accept</button>
          <button onClick={this.rejectClicked}>reject</button>
        </div>

      </div>
    );
  }
}

export default ProjectDetail;