import React from 'react';
import AudioPlayer from 'react-h5-audio-player';
import 'react-h5-audio-player/lib/styles.css';


class Commit extends React.Component {
      constructor(props){
        super(props);
        this.player =React.createRef();
      }
      state = {
        url: "/"+this.props.projectID+"/"+this.props.commitID+".mp3",
        check: false
      } 
      componentDidMount(){
        if(this.props.master){
          console.log(typeof this.props.last_update)
          this.setState({url: "/"+this.props.projectID+"/master_"+this.props.last_update.getTime()+".mp3"})
          console.log(this.props.last_update.getTime());
        }
      }
      componentDidUpdate(prevProps) {
        if(this.props.last_update !== prevProps.last_update) // Check if it's a new user, you can also use some unique property, like the ID  (this.props.user.id !== prevProps.user.id)
        {
          this.setState({url: "/"+this.props.projectID+"/master_"+this.props.last_update.getTime()+".mp3"})
          console.log(this.props.last_update.getTime());
          this.player.current.audio.current.pause();
        }
        
      }


      onCheckBox=(e)=>{ // request냐 commit이냐에 따라 onCheck, onUncheck로 주어지는 게 다름.
          if(this.state.check){
            console.log('commit checked->unchecked')
            //checked->unchecked
            this.setState({check:false})
            //onUncheck로넘겨받은 commitItemUnchecked or requestItemUnchecked 이용
            if(this.props.type==='commit'){
              this.props.onUncheck(this.props.commitID)
            } else {this.props.onUncheck(this.props.commitID, this.props.artistID)}
            
          } else {
            console.log('commit unchecked->checked')
            //unchecked->checked
            this.setState({check:true})
            //onCheck로넘겨받은 commitItemChecked or requestItemChecked 이용
            if(this.props.type==='commit'){
              this.props.onCheck(this.props.commitID)
            } else {this.props.onCheck(this.props.commitID, this.props.artistID)}
          }
      }
      
      
      render() {
        
        const style = {
          borderRadius: '10px',
          backgroundColor: 'black',
          color: 'white',
          padding: '8px',
          margin: '8px'
        };
        
        return (
          <div style={style}>
            <div>{this.props.commitID}<input type="checkbox" onClick={this.onCheckBox}></input></div>
            <div style={{fontSize: '20px', textAlign:'right'}}>{this.props.artistID}</div>
            <div style={{fontSize: '20px', textAlign:'right'}}>{this.props.category}</div>
            <AudioPlayer
            autoPlay={false}
            ref={this.player}
            layout="horizontal"
            src={this.state.url}
            onPlay={e => console.log("onPlay")}
            // other props here
            />
          </div>
        );
      }
}

export default Commit;