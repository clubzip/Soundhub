import React from 'react';
import '../App.css';
import AudioPlayer from 'react-h5-audio-player';
import 'react-h5-audio-player/lib/styles.css';

class RequestPlayer extends React.Component {
    state = {
        url: "/"+this.props.projectID+"/"+this.props.target+".mp3"
    }
    render() {
    return (
        <div style={{width:'550px'}}>
        <AudioPlayer
            layout="horizontal"
            src={this.state.url}
            onPlay={e => console.log("onPlay")}
            // other props here
        />
        </div>
    );
    }
}

export default RequestPlayer;