import React from 'react';
import '../App.css';
import Player from './Player';


class Commit extends React.Component {
    // static defaultProps = {
    //     info: {
    //       name: '이름',
    //       phone: '010-0000-0000',
    //       id: 0
    //     },
    //   }
      render() {
        console.log(this.props.commitID);    
        const style = {
          borderRadius: '10px',
          backgroundColor: 'black',
          color: 'white',
          padding: '8px',
          margin: '8px'
        };
        
        return (
          <div style={style}>
            <div>{this.props.commitID}</div>
            <div style={{fontSize: '20px', textAlign:'right'}}>{this.props.artistID}</div>
            <div style={{fontSize: '20px', textAlign:'right'}}>{this.props.category}</div>
            <Player projectID={this.props.projectID} target={this.props.commitID}/>
          </div>
        );
      }
}

export default Commit;