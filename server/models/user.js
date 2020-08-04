var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema({
    facebookID: String,
    googleID: String,
    userid: String,
    email: String,
    password: String,
    projects:[String], // projects which are made by this user
    commit_projects:[String] // projects which are commited by this user
});

module.exports = mongoose.model('user', userSchema);