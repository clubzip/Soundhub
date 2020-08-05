import React, { Component } from 'react';
import Commit from './Commit';
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
    
    //clickedlist에 commitID 추가 후 pause 실행 및 reset
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.concat(commitID)}, ()=>{
      console.log(this.state.clickedlist);
      this.pause();
      // 추가된 리스트 reset
      for(var commit of this.state.clickedlist){
        this.state.refsCollection[commit].player.current.audio.current.currentTime = 0
      }
    })
  
    
  }
  commitItemUnchecked = (commitID) => {
    //pause 실행 및 reset
    this.pause();
    for(var commit of this.state.clickedlist){
      this.state.refsCollection[commit].player.current.audio.current.currentTime = 0
    }
    //clickedlist에 commitID 제거
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.filter(info=> info !== commitID)}, ()=>{console.log(this.state.clickedlist)})
    console.log(this.state.clickedlist)
  }
  requestItemChecked = (commitID, artistID) => {
    
    //clickedlist에 commitID 추가 후 pause 실행 후 reset
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.concat(commitID)}, ()=>{
      this.pause();
      console.log(this.state.clickedlist)
      //추가된 리스트 reset
      for(var commit of this.state.clickedlist){
        this.state.refsCollection[commit].player.current.audio.current.currentTime = 0
      }
    })
    //requestlist에 commitID 추가
    const origin_request_clickedlist = this.state.request_clickedlist;
    this.setState({request_clickedlist: origin_request_clickedlist.concat({"commitID":commitID,"artistID":artistID})}, ()=>{console.log(this.state.request_clickedlist)})   
  }
  requestItemUnchecked = (commitID, artistID) => {
    
    //clickedlist에 commitID 제거
    const origin_clickedlist = this.state.clickedlist;
    this.setState({clickedlist: origin_clickedlist.filter(info=> info !== commitID)}, ()=>{console.log(this.state.clickedlist)})
    //requestlist에 commitID 제거
    const origin_request_clickedlist = this.state.request_clickedlist;
    this.setState({request_clickedlist: origin_request_clickedlist.filter(info=> info.commitID !== commitID)}, ()=>{console.log(this.state.request_clickedlist)})
    //pause 실행 및 reset
    this.pause();
    for(var commit of this.state.clickedlist){
      this.state.refsCollection[commit].player.current.audio.current.currentTime = 0
    }
  }

  acceptClicked = (e) => {
    //request_clickedlist와 projectID body에 넣고 /api/request/accept로 post요청
    const url = 'http://localhost:3001/api/request/accept';
    post(url,{projectID:this.props.projectID, acceptlist:this.state.request_clickedlist})
    .then((response) => {
      console.log(response.data.message)
    })
    // 서버
    // - 해당 commit을 users collection의 request_list에서 commit_list로 옮김
    // - 해당 commit을 projects collection의 requests에서 commits로
    // - 옮기고 last update 갱신 & master로 merge
  }

  rejectClicked = (e) => {
    //request_clickedlist와 groupID를 body에 넣고 /api/request/reject로 post요청
    const url = 'http://localhost:3001/api/request/reject';
    post(url,{projectID:this.props.projectID, rejectlist:this.state.request_clickedlist})
    .then((response) => {
      console.log(response.data.message)
    })
    // 서버
    // - 해당 commit을 users collection의 request_list에서 삭제
    // - 해당 commit을 projects collection의 requests에서 삭제
  }

  playClicked = (e) => {
    //clickedlist에 있는 녀석들(i)을 this.refsCollection[i].player.current.audio.play()
    for(var commitID of this.state.clickedlist){
      console.log(commitID)
      this.state.refsCollection[commitID].player.current.audio.current.play()
    }
    //this.state.play = true
    this.setState({play:true},()=>{console.log(this.state.play)})
    
  }
 
  pause=()=>{
    //clickedlist에 있는 녀석들(i)을 this.refsCollection[i].player.current.audio.pause()
    for(var commitID of this.state.clickedlist){
      console.log(commitID)
      this.state.refsCollection[commitID].player.current.audio.current.pause()
    }
    //this.state.play = false
    this.setState({play:false},()=>{console.log(this.state.play)})
  }

  pauseClicked = (e) => {
    this.pause()
    //pause 실행
  }

  resetClicked = (e) => {
    
    // var totallist = this.state.commitlist.concat(this.state.requestlist);
    //clickedlist에 있는 녀석들(i)을 this.refsCollection[i].player.current.audio.play()
    for(var commitID in this.state.refsCollection){
      console.log(commitID)
      this.state.refsCollection[commitID].player.current.audio.current.pause()
      this.state.refsCollection[commitID].player.current.audio.current.currentTime = 0
    }
    //this.state.play = false
    this.setState({play:false},()=>{console.log(this.state.play)})
    
  }
  
  render() {
    const commitlist = this.state.commitlist;
    const requestlist = this.state.requestlist;
    
    const CommitList = commitlist.map(
      info => (
        <Commit
          type='commit'
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
          type='request'
          onCheck={this.requestItemChecked}
          onUncheck={this.requestItemUnchecked}
          ref={(instance)=>{this.state.refsCollection[info.commitID] = instance}}
          commitID={info.commitID}
          projectID={this.props.projectID}
          artistID={info.artistID}
          category={info.category}
        />)
    );
    const adminstring = this.state.adminlist.join();

    return (
      <div>
        {/* Master player */}
        <div>
          <Commit
            master={true}
            onCheck={this.commitItemChecked}
            onUncheck={this.commitItemUnchecked}
            ref={(instance)=>{this.state.refsCollection['Master'] = instance}}
            commitID='Master'
            projectID={this.props.projectID}
            artistID={adminstring}
            category={this.props.projectID}
          />
        </div>

        {/* Commit list */}
        <div>
          Commit list
          {CommitList}
        </div>
        
        {/* Request list */}
        {
        this.state.adminlist.includes($.cookie("login_userid"))
        ?
        <div>Request list{RequestList}</div>
        :
        null
        }
        
        {/* Master player */}
        <div>
          <button onClick={this.playClicked}>play</button>
          <button onClick={this.pauseClicked}>pause</button>
          <button onClick={this.resetClicked}>reset</button>          
        </div>
        {/* Administrator's project manager */}
        {
          this.state.adminlist.includes($.cookie("login_userid"))
          ?
          <div>
          <button onClick={this.acceptClicked}>accept</button>
          <button onClick={this.rejectClicked}>reject</button>
          </div>
          :
          null
        }

      </div>
    );
  }
}

export default ProjectDetail;