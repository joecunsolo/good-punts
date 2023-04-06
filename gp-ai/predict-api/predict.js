const { PythonShell } = require("python-shell");
const express = require("express");
const bodyParser = require("body-parser");

// Create an instance of the Express server
const app = express();
app.use(bodyParser.json());

// Define a POST route for making predictions
app.post("/predict", (req, res) => {
    // Get the features from the request body
    const features = req.body;
    console.log(features);
    // Execute the Python script that loads the trained model and makes a prediction
    const pyshell = new PythonShell("python/predict.py");
    pyshell.send(JSON.stringify(features));
    pyshell.on("message", (message) => {
        // Parse the prediction from the response and send it back to the client
        //const prediction = parseFloat(message);
        res.json({ message });
    });
    

    //Here are the option object in which arguments can be passed for the python_test.js.
    /*let options = {
        mode: 'text',
        pythonOptions: ['-u'], // get print results in real-time
        scriptPath: 'python', //If you are having python_test.py script in same folder, then it's optional.
        args: ['shubhamk314'] //An argument which can be accessed in the script using sys.argv[1]
    };

    PythonShell.run('predict.py', options, function (err, result){
          if (err) throw err;
          // result is an array consisting of messages collected
          //during execution of script.
          console.log('result: ', result.toString());
          res.send(result.toString())
    });*/
});

// Start the server
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
