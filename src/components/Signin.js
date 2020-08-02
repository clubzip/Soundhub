import React from 'react';
import '../App.css';
import Modal from 'react-bootstrap/Modal'
import { Button, Form, FormGroup, Label, Input} from 'reactstrap';
import { FacebookLoginButton, GoogleLoginButton} from 'react-social-login-buttons'
import FacebookLogin from 'react-facebook-login/dist/facebook-login-render-props'
import GoogleLogin from 'react-google-login';
import { post } from 'axios';
import $ from "jquery";
import {} from "jquery.cookie";

// import TiSocialFacebookCircular from 'react-icons/lib/ti/social-facebook-circular';


const btnStyle = {
  color: "white",
  background: "teal",
  padding: ".375rem .75rem",
  border: "1px solid teal",
  borderRadius: ".25rem",
  fontSize: "1rem",
  lineHeight: 1.5,
}


class Signin extends React.Component {

  constructor() {
    super()
    this.state={
      show:false,
      showFailed:false,
      email:'',
      password:'',
      facebookID:'',
      googleID:'',
      signin:true,
      duplicated:false
    }
  }


  handleModal(){
    this.setState({show:!this.state.show,
                    showFailed:false,
                    email:'',
                    password:'',
                    facebookID:'',
                    googleID:'',
                    signin:true})
  }

  signIn = ()=>{
    const url = 'http://localhost:3001/api/signin';
    return post(url,{email: this.state.email, password: this.state.password});
  }
  signUp = ()=>{
    const url = 'http://localhost:3001/api/signup';
    if (this.state.facebookID!==''){
        return post(url+'/facebook',{email: this.state.email, facebookID: this.state.facebookID});
    } else {
        return post(url+'/google',{email: this.state.email, googleID: this.state.googleID});
    }
  }

  handleSignIn = (e) => {
    e.preventDefault()
    if(this.state.signin){
        this.signIn()
        .then((response) => {
          if(response.data.message === "succeeded"){ // 로그인 성공
            console.log('asdfsadf'); 
            this.setState({
              email:'',
              password:'',
              showFailed:false,
              show:false,
            });
            $.cookie("login_email", response.data.email, { expires: 1 });
            // window.sessionStorage.setItem('login_email', response.data.email);
            window.location.reload();
            // -- page reload & modal 종료(show=false)
            // -- 메인 페이지의 컴포넌트들 특히 navigation bar같은 영역에선 반드시
            // -- 쿠키의 존재여부로 UI를 분리해야함. 존재하면 Sign In, Sign Up버튼 없애고 
            // -- Mypage 생김. My page쪽으로 넘어갈 때 유저 데이터 기반으로
            // -- Mypage 구성후 보여줘야 함.
          } else {// 로그인 실패
            console.log('asdfaaaaaaaasadf'); 
            document.getElementById('email').style.border = '1px solid red';
            document.getElementById('password').style.border = '1px solid red';
            this.setState({
              email:'',
              password:'',
              showFailed:true
            });
            //input 들 빨간색 테두리로 변하고
            // 밑에 ID나 비밀번호가 틀렸다고 띄우기
          }
          console.log(response.data);
        })
        .catch(function (error) {
          
          console.log(error);
        });   
    } else {
        this.signUp()
        .then((response) => {
            if(response.data.message === "succeeded"){ // 회원가입 및 로그인 성공
                // console.log('asdfsadf'); 
                this.setState({
                  email:'',
                  password:'',
                  signin:true,
                  facebookID:'',
                  googleID:'',
                  showFailed:false,
                  show:false,
                });
                $.cookie("login_email", response.data.email, { expires: 1 });
                // window.sessionStorage.setItem('login_email', response.data.email);
                window.location.reload();
              } else {// 회원가입 실패
                document.getElementById('email').style.border = '1px solid red';
                this.setState({
                    email:'',
                    password:'',
                    duplicated:true
                  }); 
              }
        });   
    } 
  }

  handleValueChange = (e) => {
    let nextState = {};
    nextState[e.target.name] = e.target.value;
    this.setState(nextState);
  }

  

  render() {

    const responseFacebook = (response) => {
        console.log(response);
        const url = 'http://localhost:3001/api/signin';
        const userid = response.id;
        post(url,{facebookID:userid})
        .then((response) => {
            if(response.data.message === "succeeded"){ // 로그인 성공
              console.log(response.data.email); 
              this.setState({
                email:'',
                password:'',
                showFailed:false,
                show:false,
              });
              $.cookie("login_email", response.data.email, { expires: 1 });
              console.log($.cookie("login_email"));
                // window.sessionStorage.setItem('login_email', response.data.email);
              window.location.reload();
              // -- page reload & modal 종료(show=false)
              // -- 메인 페이지의 컴포넌트들 특히 navigation bar같은 영역에선 반드시
              // -- 쿠키의 존재여부로 UI를 분리해야함. 존재하면 Sign In, Sign Up버튼 없애고 
              // -- Mypage 생김. My page쪽으로 넘어갈 때 유저 데이터 기반으로
              // -- Mypage 구성후 보여줘야 함.
            } else {// 로그인 실패
                this.setState({
                    //sign up 모드 되게 함.
                    facebookID:userid,
                    signin:false
                });               
                //password 숨기고 Sign in -> Sign Up
            }
          })
          .catch(function (error) {         
            console.log(error);
          });    
    }
    const responseGoogle = (response) => {
        console.log(response);
        const url = 'http://localhost:3001/api/signin';
        const userid = response.googleId;
        post(url,{googleID:userid})
        .then((response) => {
            if(response.data.message === "succeeded"){ // 로그인 성공
              console.log('asdfsadf'); 
              this.setState({
                email:'',
                password:'',
                showFailed:false,
                show:false,
              });
              $.cookie("login_email", response.data.email, { expires: 1 });
            // window.sessionStorage.setItem('login_email', response.data.email);
              window.location.reload();
              // -- page reload & modal 종료(show=false)
              // -- 메인 페이지의 컴포넌트들 특히 navigation bar같은 영역에선 반드시
              // -- 쿠키의 존재여부로 UI를 분리해야함. 존재하면 Sign In, Sign Up버튼 없애고 
              // -- Mypage 생김. My page쪽으로 넘어갈 때 유저 데이터 기반으로
              // -- Mypage 구성후 보여줘야 함.
            } else {// 로그인 실패
                this.setState({
                    //sign up 모드 되게 함.
                    googleID:userid,
                    signin:false
                }); 
            }
          })
          .catch(function (error) {         
            console.log(error);
          });
    }

    return (
      <div>
        <button style={btnStyle} onClick={()=>{this.handleModal()}}>Sign In</button>
        <Modal show={this.state.show}>
        <div align="right" style={{padding:"10px"}}>
        <button type="button" className="close" aria-label="Close" onClick={()=>{this.handleModal()}}>
         <span aria-hidden="true">&times;</span>
        </button>
        </div>
        <Modal.Body>
        <Form className="login-form" onSubmit={this.handleSignIn}>
          <h1 className="text-center">BEATING</h1>
          <h2 className="text-center">Welcome</h2>
          <FormGroup>
            <Label>E-mail</Label>
            <Input id='email' type="text" placeholder="E-mail" name="email" value={this.state.email} onChange={this.handleValueChange}/>
          </FormGroup>
          {this.state.signin ?
            <FormGroup>
            <Label>Password</Label>
            <Input id='password' type="password" placeholder="Password" name="password" value={this.state.password} onChange={this.handleValueChange}/>
            </FormGroup>
            : null
          }
          {this.state.duplicated ? <Label style={{color:"red"}}>There's a duplicate E-mail.</Label> : null}
          {this.state.showFailed ? <Label style={{color:"red"}}>Please check your account again.</Label> : null}
          {this.state.signin ?
            <Button className="btn-lg btn-dark btn-block" type="submit">Sign In</Button> :
            <Button className="btn-lg btn-danger btn-block" type="submit">Sign Up</Button>
          }
          </Form>
          {this.state.signin ?
          <div className="login-form">
          <div className="text-center pt-3">
            Or continue with your social account
          </div>
            <FacebookLogin
                type="button"
                appId="335232777509293"
                callback={responseFacebook}
                render={renderProps => (
                    <FacebookLoginButton type="button" onClick={renderProps.onClick}/>
                )}
                />
            <GoogleLogin
                type="button"
                clientId="514550590190-a0c3cfno92nnudgn2l0574b7am4g9j1c.apps.googleusercontent.com"
                onSuccess={responseGoogle}
                onFailure={responseGoogle}
                render={renderProps => (
                    <GoogleLoginButton type="button" onClick={renderProps.onClick}/>
                    )}
                cookiePolicy={'single_host_origin'}
            />
          {/* <div className="text-center">
            <a href="/sign-up">Sign up</a>
            <span className="p-2">|</span>
            <a href="/forgot-password">Forgot Password</a>
          </div> */}
          </div>
          :
          <div className="text-center pt-3">
            <b>Sign up with your social account!</b>
          </div>
          }
          
      
        </Modal.Body>
      </Modal>
      </div>
      
      
    )
  }
}


export default Signin;