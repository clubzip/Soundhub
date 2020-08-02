var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var projectSchema = new Schema({
    projectID: String,
    description: String,
    like: Number,
    last_update: Date,
    commits: [{artistID:String, commitID:String, category: String}]
});

module.exports = mongoose.model('project', projectSchema);