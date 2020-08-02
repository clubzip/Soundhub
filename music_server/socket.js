var express = require('express');
var app = express();
var socketio = require('socket.io');

var server = app.listen(3040, ()=>{
    console.log('Listening at port number 3040...');
});

//express 서버를 socket io 서버로 업그레이드
var io = socketio.listen(server);

//누가 chatroom에 있는지
var whoIsOn = [];

io.on('connection', function(socket){

    console.log('socket io connected');
    var nickname = '';

    socket.on('chat-message',function(data){
        console.log('chat-message received and emit');
        socket.emit('chat-message', data);
    });



});