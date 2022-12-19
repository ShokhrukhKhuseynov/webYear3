const express = require('express');
const url = require("url");
const db = require("./database");
const app = express();

require("./http_status.js");



// app.get('/laptops/*', handleGetRequest);
app.get('/search', handleGetRequest);
app.get('/compare/*', handleGetRequest);


app.use(express.static('public'));
app.listen(8080);

console.log("server is running");
/* Handles GET requests sent to web service.
   Processes path and query string and calls appropriate functions to
   return the data. */
function handleGetRequest(request, response) {
    //Parse the URL
    const urlObj = url.parse(request.url, true);
    //Extract object containing queries from URL object
    const queries = urlObj.query;

    //Get searching properties if they have been set. Will be undefined if not set
    const brand = queries.brand;
    const model = queries.model;
 
    //Get the pagination properties if they have been set. Will be undefined if not set
    const numItems = queries["num_items"];
    const offset = queries["offset"];
    const page = queries["page"];
    
    //Split the path of the request into its components
    const pathArray = urlObj.pathname.split("/");

    //Get the last part of the path
    const pathEnd = pathArray[pathArray.length - 1];
    
    var regEx = new RegExp('^[0-9]+$');//RegEx returns true if string is all digits.
  
    if(brand || model){
        db.getLaptopsByParameters(response,brand, model, numItems, page);
        return;
    }

    //If the last part of the path is a valid laptop id, return comparison data about that laptop
    if(regEx.test(pathEnd) && pathArray[pathArray.length - 2] === "compare"){
        db.getComparison(response, pathEnd);
        return;
    }

    //The path is not recognized. Return an error message
    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}

module.exports = app;

