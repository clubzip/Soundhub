// server/routes/index.js


module.exports = function(router,User, Project, storage, multer, SoxCommand)
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
                if(result === null) return res.json({error: 'records not found'});
                console.log(result.email);
                res.json({message:"succeeded", email:result.email, userid:result.userid});
        });
    });
    router.post('/api/signup', (req, res) => {
        console.log('dfddddddddd');
        User.find({userid:req.body.userid}, function(err, result){
            if(err) return res.status(500).json({error: err});
            if(result.length != 0) return res.json({error: 'There is a duplicate ID'});
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
            if(result.length != 0) return res.json({error: 'There is a duplicate ID'});
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
            if(result.length != 0) return res.json({error: 'There is a duplicate ID'});
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

    
    router.post('/api/upload', multer({storage:storage}).single('files'),(req, res) => {
        console.log('here');
        console.log(req.body.commitID);
        Project.findOne({projectID:req.body.projectID} , function(err, result) {
            console.log('hhhhhhhhheeeeerrrr');
            if(result.admin.includes(req.body.userid)) {
                //1. Project commits에 artistID:, commitID, category 추가
                console.log("upload if branch");
                    var json = {artistID:req.body.userid, commitID:req.body.commitID, category:req.body.category}
                    console.log(json);
                    console.log(req.body.projectID);
                    Project.updateOne({projectID:req.body.projectID},{$push:{commits:json}}, function(err, result){
                        if (err) {
                            console.log(err);
                        } else {
                            console.log(result);
                        }
                    });
                // 2. master.mp3랑 합치기
               

            } else {
                console.log("upload else branch");
                // 1. Project requests 에 artistID:, commitID, category 추가
                    var json = {artistID:req.body.userid, commitID:req.body.commitID, category:req.body.category}
                    Project.updateOne({projectID:req.body.projectID},{$push:{requests:json}}, function(err, result){
                        if (err) {
                            console.log(err);
                        } else {
                            console.log(result);
                        }
                    });
                // 2. master+commitID.mp3 파일 생성

                // 3. commit한 User의 commit projects에 이 프로젝트ID 추가
            }
        } )


    });

}
