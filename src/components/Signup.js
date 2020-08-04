import React from 'react';
import '../App.css';
import Modal from 'react-bootstrap/Modal'
import { Button, Form, FormGroup, Label, Input} from 'reactstrap';
import { post } from 'axios';
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

class Signup extends React.Component {
    constructor() {
        super()
        this.state={
          show:false,
          userid:'',
          email:'',
          password:'',
          showFailed:false
        }
    }
    handleModal(){
        this.setState({show:!this.state.show,
                        email:'',
                        userid:'',
                        password:'',
                        showFailed:false});
    }
    handleValueChange = (e) => {
        let nextState = {};
        nextState[e.target.name] = e.target.value;
        this.setState(nextState);
    }
    signUp = ()=>{
        const url = 'http://localhost:3001/api/signup';
        return post(url,{email: this.state.email, userid:this.state.userid, password: this.state.password});
    }
    handleSignUp = (e) => {
        e.preventDefault()
        this.signUp()
        .then((response) => {
            if(response.data.message === "succeeded"){ // 회원가입 및 로그인 성공
                // console.log('asdfsadf'); 
                this.setState({
                    email:'',
                    userid:'',
                    password:'',
                    showFailed:false,
                    show:false,
                });
                $.cookie("login_userid", response.data.userid, { expires: 1 });
                $.cookie("login_email", response.data.email, { expires: 1 });
                // window.sessionStorage.setItem('login_email', response.data.email);
                window.location.reload();
                } else {// 회원가입 실패
                    document.getElementById('email').style.border = '1px solid red';
                    document.getElementById('userid').style.border = '1px solid red';
                    document.getElementById('password').style.border = '1px solid red';
                    this.setState({
                        email:'',
                        password:'',
                        userid:'',
                        showFailed:true
                    });
                }
        });   
        
    }

    render() {
        return (
            <div>
              <button style={btnStyle} onClick={()=>{this.handleModal()}}>Sign Up</button>
              <Modal show={this.state.show}>
              <Modal.Header>
                <div align="right" style={{padding:"10px"}}>
                <button type="button" className="close" aria-label="Close" onClick={()=>{this.handleModal()}}>
                <span aria-hidden="true">&times;</span>
                </button>
                </div>
              </Modal.Header>
              <Modal.Body>
              <Form className="login-form" onSubmit={this.handleSignUp}>
                <h1 className="text-center">BEATING</h1>
                <h2 className="text-center">Welcome</h2>
                <FormGroup>
                  <Label>E-mail</Label>
                  <Input id='email' type="text" placeholder="E-mail" name="email" value={this.state.email} onChange={this.handleValueChange}/>
                </FormGroup>
                <FormGroup>
                <Label>ID</Label>
                <Input id='userid' type="text" placeholder="ID" name="userid" value={this.state.userid} onChange={this.handleValueChange}/>
                </FormGroup>
                <FormGroup>
                <Label>Password</Label>
                <Input id='password' type="password" placeholder="Password" name="password" value={this.state.password} onChange={this.handleValueChange}/>
                </FormGroup>
                {this.state.showFailed ? <Label style={{color:"red"}}>There's a duplicate ID.</Label> : null}
                <Button className="btn-lg btn-danger btn-block" type="submit">Sign Up</Button>
                </Form>            
              </Modal.Body>
            </Modal>
            </div>
        )
    }
}

export default Signup;