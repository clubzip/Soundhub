import React, { Component } from 'react';
import Request from './Request';

class RequestList extends Component {
    
  render() {
    const data = this.props.data;
    console.log(data);
    const list = data.map(
      info => (
        <Request
          commitID={info.commitID}
          projectID={this.props.projectID}
          artistID={info.artistID}
          category={info.category}
        />)
    );

    return (
      <div>
        {list}    
      </div>
    );
  }
}

export default RequestList;