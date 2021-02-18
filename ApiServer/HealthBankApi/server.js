const express = require("express");


const app = express();

const PORT = process.env.PORT || 8000;


const MongoClient = require('mongodb').MongoClient;
const url = 'mongodb://localhost:27017/healthBank';


MongoClient.connect(url, { useUnifiedTopology: true }).then(client => {
      console.log('Connected to Database')
      const db = client.db('healthBank')



      app.get('/users', (req, res) => {
    		db.collection('Users').find().toArray((err, result) => {
      		if (err) {
      			return console.log(err)
      		}
      		res.status(202).send({data: result})
    		})
  	  })


      app.get('/users/:personId', (req, res) => {
    		db.collection('Users').find({"personId":req.params.personId}).toArray((err, result) => {
      		if (err) {
      			return console.log(err)
      		}
      		if (Object.keys(result).length === 0){
      			return res.status(202).send("No such user")
      		}
          console.log("Success!")
      		res.status(202).send({data: result[0]})
    		})
  	  })



  	  app.listen(PORT, () => {
    		console.log('Listening on port 8000...');
  	  });

  })

