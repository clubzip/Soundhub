var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var projectSchema = new Schema({
    create_date: {type:Date, default: Date.now},
    projectID: String,
    description: String,
    like: Number,
    last_update: Date,
    commits: [{date: {type: Date, default:Date.now()}, artistID:String, commitID:String, category: String}],
    requests: [{date: {type: Date, default:Date.now()}, artistID:String, commitID:String, category: String}],
    admin:[String] // list of artistID who can modify the project.
});

module.exports = mongoose.model('project', projectSchema);