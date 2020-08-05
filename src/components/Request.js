import React from 'react';
import RequestPlayer from './RequestPlayer';


class Request extends React.Component {

      render() {
        const style = {
          borderRadius: '10px 10px 0px 0px',
          backgroundColor: 'black',
          width: '700px',
          color: 'white',
          padding: '8px',
          margin: '8px',
          marginBottom: '0px'
        };
        const accept ={
          float:'left',
          width: '350px',
          border:'3px solid',
          borderRight:'1.5px solid',
          borderRadius: '0px 0px 0px 10px'
        }
        const reject ={
          float:'right',
          width: '350px',
          border:'3px solid',
          borderLeft:'1.5px solid',
          borderRadius: '0px 0px 10px 0px',
        }
        
        return (
          <div>
            <div style={style}>
              <div>{this.props.commitID}</div>
              <div style={{fontSize: '20px', textAlign:'right'}}>{this.props.artistID}</div>
              <div style={{fontSize: '20px', textAlign:'right'}}>{this.props.category}</div>
              <div style={{display:'flex'}}>
                <div style={{width: '180px'}}>Commit -</div>
                <RequestPlayer projectID={this.props.projectID} target={this.props.commitID}/>
              </div>
              <div style={{display:'flex'}}>
                <div style={{width: '180px'}}>Merged -</div>
                <RequestPlayer projectID={this.props.projectID} target={'master_'+this.props.commitID}/>
              </div>
            </div>
            <div style={{ width:'700px', marginLeft:'8px', marginRight:'8px' }}>
              <button style={accept}>ACCEPT</button>
              <button style={reject}>REJECT</button>
            </div>
          </div>
        );
      }
}

export default Request;