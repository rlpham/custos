const express = require('express');
const app = express();
var firebase = require('firebase/app');
require("firebase/auth");
require("firebase/database");

const firebaseConfig = {
    apiKey: "AIzaSyBOXjbRugPnYicZMKV_bC3qsu1UpA5kP-M",
    authDomain: "custos-267122.firebaseapp.com",
    databaseURL: "https://custos-267122.firebaseio.com",
    projectId: "custos-267122",
    storageBucket: "custos-267122.appspot.com",
    messagingSenderId: "906240176306",
    appId: "1:906240176306:web:b3f5937f4a5d840e3719ab",
    measurementId: "G-L71ZLWZVTL"
};

firebase.initializeApp(firebaseConfig);

app.set('view engine', 'ejs');

app.get("/", (req, res) => {
    //res.send("HELLO M8");
    res.render("pages/index");
});

app.post("/", (req, res) => {
    res.send("HELLO M8");
})

app.listen(8080);