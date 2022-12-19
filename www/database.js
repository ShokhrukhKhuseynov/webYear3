const mysql = require('mysql');
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "db_web_scraping",
    debug: false
});

/** Returns comparisons with the specified laptop ID */
function getComparison(response, id) {
    //Build SQL query to select laptop with specified id.
    let sql = "SELECT * FROM comparison WHERE laptop_id=" + id;

    //Execute the query
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err) {
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({ 'error': true, 'message': + err });
            return;
        }

        //Output results in JSON format
        response.json(result);
    });
}

exports.getLaptopsByParameters = (response, brand, model, numItems, page) => {
    //Select the laptops data using JOIN to convert foreign keys into useful data.
    let sql = "SELECT * FROM (SELECT laptop.id, laptop.brand, laptop.model, laptop.description, laptop.image_url, laptop.year, laptop.ram, laptop.ssd, laptop.cpu, laptop.screen, comparison.price FROM laptop JOIN comparison ON laptop.id = comparison.laptop_id";

    if (brand !== 'undefined' && model !== 'undefined') {
        sql += " WHERE brand='" + brand + "' AND model='" + model + "'";
    } else if (brand !== 'undefined') {
        sql += " WHERE brand='" + brand + "'";
    }
    sql += " ORDER BY comparison.price ASC LIMIT 99) AS sub GROUP BY sub.id ORDER BY sub.price LIMIT " + page * numItems + ", " + numItems;

    //Execute the query
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err) {
            console.log(err);
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({ 'error': true, 'message': + err });
            return;
        }
        const returnObj = { totalNumberOfLaptops: result.length, data: result };//Array of data from database

        //Return results in JSON format
        response.json(returnObj);
    });
}
//export function
module.exports.getComparison = getComparison;

