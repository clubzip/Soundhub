import React, { Component } from 'react';
import Commit from './Commit';

class CommitList extends Component {
    
  render() {
    const data = this.props.data;
    console.log(data);
    const list = data.map(
      info => (
        <Commit
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

export default CommitList;