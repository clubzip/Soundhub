import React from 'react';
import '../App.css';
import Modal from 'react-bootstrap/Modal'
import { Button, Form, FormGroup, Label, Input} from 'reactstrap';
import $ from "jquery";
import {} from "jquery.cookie";

const btnStyle = {
    color: "white",
    background: "teal",
    padding: ".375rem .75rem",
    border: "1px solid teal",
    borderRadius: ".25rem",
    fontSize: "1rem",
    lineHeight: 1.5,
  }
  

  
class Signout extends React.Component {

    signOut = (e) => {
        console.log($.cookie("login_email"));
        $.cookie('login_email', null);
        console.log($.cookie("login_email"));
        window.location.reload();
    }
    
    render() {
    return (
        <div>
            <button style={btnStyle} onClick={()=>{this.signOut()}}>Sign Out</button>
        </div>
        )
}
}

export default Signout;