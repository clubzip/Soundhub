var mongoose = require('mongoose');
const { type } = require('jquery');
var Schema = mongoose.Schema;

var userSchema = new Schema({
    facebookID: String,
    googleID: String,
    userid: String,
    email: String,
    password: String,
    projects:[String], // projects which are made by this user
    commit_list:[{projectID:String, commitID: String, category: String, date:{type: Date, default:Date.now()}}], // projects which are commited by this user
    request_list:[{projectID:String, commitID: String, category: String, date:{type: Date, default:Date.now()}}] // projects which are commited by this user
});

module.exports = mongoose.model('user', userSchema);