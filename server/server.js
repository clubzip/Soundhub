// server/server.js

const express = require('express');
const bodyParser = require('body-parser');
const app = express();


const port = process.env.PORT || 3001;
const cors = require('cors');

var mongoose    = require('mongoose');
//////////////////////////
var multer = require('multer');
var fs = require('fs');
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
        var mkdirp = require('mkdirp');
        if(!fs.existsSync(`./public/${req.body.projectID}`)){
          mkdirp(`./public/${req.body.projectID}`);
        }
        cb(null, './public/'+req.body.projectID+'/');
    },
    filename: function(req, file, cb) {
      cb(null, req.body.commitID+'.mp3');
    }
  });

// var create = multer({dest:'./public/',
//                       changeDest: function(dest,req,res){
//                         var newDestination = dest+req.body.projectID;
//                         var stat = null;
//                         try {
//                             stat = fs.statSync(newDestination);
//                         } catch (err) {
//                             fs.mkdirSync(newDestination);
//                         }
//                         if (stat && !stat.isDirectory()) {
//                             throw new Error('Directory cannot be created because an inode of a different type exists at "' + dest + '"');
//                         }
//                         return newDestination;
//                       }})

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

var User = require('./models/user');
var Project = require('./models/project');
var router = require('./routes')(app, User, Project, storage, multer);


app.listen(port, () => console.log(`Node.js Server is running on port ${port}...`));