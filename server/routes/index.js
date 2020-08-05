// server/routes/index.js


module.exports = function(router,User, Project, storage, multer, path)
{

    router.post('/api/project/detail', (req, res) => {
        console.log(req.body.projectID);
        Project.findOne({projectID:req.body.projectID}, function(err, result){
            res.json(result);
        })
    });

    router.post('/api/signin', (req, res) => {
        console.log(req.body.password);
        // console.log('here');
        // res.json({message:'hi'});
        console.log(req.body.email);
        User.findOne({$or:[{userid:{$exists: true, $eq: req.body.userid}, password:{$exists: true, $eq: req.body.password}},
            {facebookID:{$exists: true, $eq: req.body.facebookID}},{googleID:{$exists: true, $eq: req.body.googleID}}]},
            function(err, result){
                console.log(result);
                if(err) return res.status(500).json({error: err});
                console.log('herebbb');
                if(result === null) return res.json({message: 'records not found'});
                console.log(result.email);
                res.json({message:"succeeded", email:result.email, userid:result.userid});
        });
    });
    router.post('/api/signup', (req, res) => {
        console.log('dfddddddddd');
        User.find({userid:req.body.userid}, function(err, result){
            if(err) return res.status(500).json({error: err});
            if(result.length != 0) return res.json({message: 'There is a duplicate ID'});
            console.log('dfffffffffffffffd');
            var newuser = new User();
            newuser.password = req.body.password;
            newuser.userid = req.body.userid;
            newuser.email = req.body.email;
            newuser.save(function(err){
                if(err) return res.status(500).json({error: err});
                res.json({message:"succeeded", email:req.body.email, userid:req.body.userid});
            });
        });
        
    });
    router.post('/api/signup/facebook', (req, res) => {
        User.find({userid:req.body.userid}, function(err, result){
            if(err) return res.status(500).json({error: err});
            if(result.length != 0) return res.json({message: 'There is a duplicate ID'});
            var newuser = new User();
            newuser.facebookID = req.body.facebookID;
            newuser.userid = req.body.userid;
            newuser.email = req.body.email;
            newuser.save(function(err){
                if(err) return res.status(500).json({error: err});
                res.json({message:"succeeded", email:req.body.email, userid:req.body.userid});
            });
        });
    });
    router.post('/api/signup/google', (req, res) => {
        User.find({userid:req.body.userid}, function(err, result){
            if(err) return res.status(500).json({error: err});
            if(result.length != 0) return res.json({message: 'There is a duplicate ID'});
            var newuser = new User();
            newuser.googleID = req.body.googleID;
            newuser.userid = req.body.userid;
            newuser.email = req.body.email;
            newuser.save(function(err){
                if(err) return res.status(500).json({error: err});
                res.json({message:"succeeded", email:req.body.email, userid:req.body.userid});
            });
        });
    });

    
    router.post('/api/upload', multer({storage:storage}).single('files'), async (req, res) => {
        console.log('here');
        console.log(req.body.commitID);
        Project.findOne({projectID:req.body.projectID} , function(err, result) {
            console.log('hhhhhhhhheeeeerrrr');
            if(result.admin.includes(req.body.userid)) {
                var now = Date.now();
                //1. Project commits에 {date:, artistID:, commitID, category} 추가 & project lastupdated 수정
                console.log("upload admin branch");
                var commit_project = {date: now, artistID:req.body.userid, commitID:req.body.commitID, category:req.body.category}
                console.log(commit_project);
                console.log(req.body.projectID);
                Project.updateOne({projectID:req.body.projectID},{$push:{commits:commit_project}, $set:{last_update:now}}, function(err, result){
                    if (err) {
                        console.log(err);
                    } else {
                        console.log(result);
                    }
                });
                // 2. master.mp3랑 합치기
                const NodeCrunker = require('node-crunker');
                const audio = new NodeCrunker();
                // var absolute1 = path.resolve('../../public/'+req.body.projectID+'/master.mp3');
                // var absolute2 = path.resolve('../../public/'+req.body.projectID+'/'+req.body.commitID+'.mp3')

                audio
                    .fetchAudio('http://localhost:3000/'+req.body.projectID+'/master.mp3','http://localhost:3000/'+req.body.projectID+'/'+req.body.commitID+'.mp3')
                    .then(buffers => audio.mergeAudio(buffers))
                    .then(merged => audio.export(merged, './public/'+req.body.projectID+'/master.mp3'))
                    .catch(error => {
                        console.log(error);
                    });               
                //3. User의 commit_list에 {projectID:String, commitID: String, category: String, date:Date} 추가
                var commit_user = {date: now, projectID:req.body.projectID, commitID:req.body.commitID, category:req.body.category}
                User.update({userid:req.body.userid},{$push:{commit_list:commit_user}}, function(err, result){
                    if(err) {console.log(err)} else { console.log(result)}
                });

            } else {
                console.log("upload not admin branch");
                var now = Date.now();
                // 1. Project requests 에 {date, artistID:, commitID, category} 추가
                    var request_project = {date: now, artistID:req.body.userid, commitID:req.body.commitID, category:req.body.category}
                    Project.updateOne({projectID:req.body.projectID},{$push:{requests:request_project}}, function(err, result){
                        if (err) {
                            console.log(err);
                        } else {
                            console.log(result);
                        }
                    });
                // 2. master+commitID.mp3 파일 생성
                    const NodeCrunker = require('node-crunker');
                    const audio = new NodeCrunker();

                    audio
                        .fetchAudio('http://localhost:3000/'+req.body.projectID+'/master.mp3','http://localhost:3000/'+req.body.projectID+'/'+req.body.commitID+'.mp3')
                        .then(buffers => audio.mergeAudio(buffers))
                        .then(merged => audio.export(merged, './public/'+req.body.projectID+'/master_'+req.body.commitID+'.mp3'))
                        .catch(error => {
                            console.log(error);
                        });  

                // 3. request한 User의 request_list에 {projectID:String, commitID: String, category: String, date:Date} 추가
                    var request_user = {date: now, projectID:req.body.projectID, commitID:req.body.commitID, category:req.body.category}
                    User.update({userid:req.body.userid},{$push:{request_list:request_user}}, function(err, result){
                        if(err) {console.log(err)} else { console.log(result)}
                    });
                        
                    
            }
        } )


    });

}
