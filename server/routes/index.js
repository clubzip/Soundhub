// server/routes/index.js


module.exports = function(router,User, Project)
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
        User.findOne({$or:[{email:{$exists: true, $eq: req.body.email}, password:{$exists: true, $eq: req.body.password}},
            {facebookID:{$exists: true, $eq: req.body.facebookID}},{googleID:{$exists: true, $eq: req.body.googleID}}]},
            function(err, result){
                console.log(result);
                if(err) return res.status(500).json({error: err});
                console.log('herebbb');
                if(result === null) return res.json({error: 'records not found'});
                console.log(result.email);
                res.json({message:"succeeded", email:result.email});
        });
    });
    router.post('/api/signup', (req, res) => {
        console.log('dfddddddddd');
        User.find({email:req.body.email}, function(err, result){
            if(err) return res.status(500).json({error: err});
            if(result.length != 0) return res.json({error: 'There is a duplicate E-mail'});
            console.log('dfffffffffffffffd');
            var newuser = new User();
            newuser.password = req.body.password;
            newuser.email = req.body.email;
            newuser.save(function(err){
                if(err) return res.status(500).json({error: err});
                res.json({message:"succeeded", email:req.body.email});
            });
        });
        
    });
    router.post('/api/signup/facebook', (req, res) => {
        User.find({email:req.body.email}, function(err, result){
            if(err) return res.status(500).json({error: err});
            if(result.length != 0) return res.json({error: 'There is a duplicate E-mail'});
            var newuser = new User();
            newuser.facebookID = req.body.facebookID;
            newuser.email = req.body.email;
            newuser.save(function(err){
                if(err) return res.status(500).json({error: err});
                res.json({message:"succeeded", email:req.body.email});
            });
        });
    });
    router.post('/api/signup/google', (req, res) => {
        User.find({email:req.body.email}, function(err, result){
            if(err) return res.status(500).json({error: err});
            if(result.length != 0) return res.json({error: 'There is a duplicate E-mail'});
            var newuser = new User();
            newuser.googleID = req.body.googleID;
            newuser.email = req.body.email;
            newuser.save(function(err){
                if(err) return res.status(500).json({error: err});
                res.json({message:"succeeded", email:req.body.email});
            });
        });
    });
}
