var express = require('express')
var app = express()
const spawn = require('child_process').spawn
const XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest
const fetch = require("node-fetch")

const fileName = "proof.py"
const randomNum = "87e4c2a29f01ea8b21d51adf7fcfbe1ed3e9a8d0f55c009bf77856f0feb39ea37fc88c392c32ed707232dfa07ca67fdd1d5aa0092ccccdc42e98829252f717310b048a3d43b41821cad25c65cee968ebed1946a57250bfe973faee1709ccfe6392f57c80b713833ce67f1b44da6ab344af6ae681ef72811f73eae94626da430c"
const funcName = "prover"

app.get('/', function (req, res) {

	const userName = req.query.userName
	const lowBound = req.query.lowBound
	const upBound = req.query.upBound
	const secret = req.query.secret
    const diseaseId = req.query.diseaseId

	const spawn = require('child_process').spawn;
	const ls = spawn('python', [fileName, funcName,lowBound,upBound,secret,randomNum])
	console.log("the userName is " + userName)

	ls.stdout.on('data', (data) => {

		  data = "{" + (data.toString()).split("{")[1]

		  var requiredObject = {
		  	"personName" : userName,
		  	"type" : "diseaseId",
		  	"diseaseId": diseaseId,
		  	"lowBound": parseInt(lowBound),
		  	"upBound" : parseInt(upBound),
		  	"proof" : data
		  }

		 


		  fetch("http://localhost:8080/verify", {
			  method: "POST", 
			  body: JSON.stringify(requiredObject)
			}).then(res => {
			  return res.json()
			  .then(function(myJson) {
    			console.log(myJson);
  			});
			

			});
		   // console.log(requiredObject)	
		   res.send(requiredObject)


	});

	ls.stderr.on('data', (data) => {
	  console.log(`stderr: ${data}`);
	});

	ls.on('close', (code) => {
	  console.log(`child process exited with code ${code}`);
	});


});

app.listen(3000, function () {
  console.log('Example app listening on port 3000!')
})



