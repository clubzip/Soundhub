// server/server.js

const express = require('express');
const bodyParser = require('body-parser');
const app = express();


const port = process.env.PORT || 3001;
const cors = require('cors');

var mongoose    = require('mongoose');
//////////////////////////
var multer, path, crypto;
multer = require('multer')
path = require('path');
crypto = require('crypto');
/////////////////////////

// CONNECT TO MONGODB SERVER
var db = mongoose.connection;
db.on('error', console.error);
db.once('open', function(){
    // CONNECTED TO MONGODB SERVER
    console.log("Connected to mongod server");
});

mongoose.connect('mongodb://localhost/10bill');


var storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, './public/'+req.body.projectID+'/');
    },
    filename: function(req, file, cb) {
      cb(null, req.body.commitID+'.mp3');
    }
  });

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

var User = require('./models/user');
var Project = require('./models/project');
var SoxCommand = require('sox-audio');
var router = require('./routes')(app, User, Project, storage, multer, path, SoxCommand);


app.listen(port, () => console.log(`Node.js Server is running on port ${port}...`));