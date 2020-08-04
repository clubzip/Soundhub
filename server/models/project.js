var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var projectSchema = new Schema({
    projectID: String,
    description: String,
    like: Number,
    last_update: Date,
    commits: [{artistID:String, commitID:String, category: String}],
    requests: [{artistID:String, commitID:String, category: String}],
    admin:[String] // list of artistID who can modify the project.
});

module.exports = mongoose.model('project', projectSchema);