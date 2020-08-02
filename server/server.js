// server/server.js

const express = require('express');
const bodyParser = require('body-parser');
const app = express();

const port = process.env.PORT || 3001;
const cors = require('cors');
var mongoose    = require('mongoose');

// CONNECT TO MONGODB SERVER
var db = mongoose.connection;
db.on('error', console.error);
db.once('open', function(){
    // CONNECTED TO MONGODB SERVER
    console.log("Connected to mongod server");
});

mongoose.connect('mongodb://localhost/10bill');





app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));
var User = require('./models/user');
var Project = require('./models/project');
var router = require('./routes')(app, User, Project);

app.listen(port, () => console.log(`Node.js Server is running on port ${port}...`));