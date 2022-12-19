require("../http_status");

//Database code that we are testing
const db = require('../database');

//Server code that we are testing
const server = require('../server');

//Set up Chai library 
const chai = require('chai');
const should = chai.should();
const assert = chai.assert;
const expect = chai.expect;

//Set up Chai for testing web service
const chaiHttp = require('chai-http');
chai.use(chaiHttp);

//Import the mysql module and create a connection pool with the user details
const mysql = require('mysql');
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "db_web_scraping",
    debug: false
});


//Wrapper for all database tests
describe('Database', () => {

    //Create random laptop details
    const tempBrand = Math.random().toString(36).substring(2, 15);
    const tempModel = Math.random().toString(36).substring(2, 15);

    //Mocha test for getAllLaptops method in database module.
    describe('#getComparison', () => {
        it('should return all comparsions that belond to specific laptop id in the database', (done) => {
            //Mock response object for test
            const response = {};

            /* When there is an error response.staus(ERROR_CODE).json(ERROR_MESSAGE) is called
               Mock object should fail test in this situation. */
            response.status = (errorCode) => {
                return {
                    json: (errorMessage) => {
                        console.log("Error code: " + errorCode + "; Error message: " + errorMessage);
                        assert.fail("Error code: " + errorCode + "; Error message: " + errorMessage);
                        done();
                    }
                }
            };

            //Add send function to mock object
            response.json = (result) => {
                // Convert result to JavaScript object
                const resObj = result[0];
             
                 //Clean up database
                 sql = "DELETE FROM comparison WHERE price=" + 0;
                 connectionPool.query(sql, (err, result) => {
                     if (err) {//Check for errors
                         assert.fail(err);//Fail test if this does not work.
                         done();//End test
                     } else {
                         sql = "DELETE FROM laptop WHERE brand='" + tempBrand + "' AND model='" + tempModel + "'";
                         connectionPool.query(sql, (err, result) => {
                             if (err) {//Check for errors
                                 assert.fail(err);//Fail test if this does not work.
                                 done();//End test
                             }
                             else {
                                console.log(resObj);
                                resObj.should.have.property('id');
                                resObj.should.have.property('laptop_id');
                                resObj.should.have.property('price');
                                resObj.should.have.property('url');
                                resObj.should.have.property('supplier');
                                expect(resObj.price).to.equal(0);
                                expect(resObj.url).to.equal("mockExample.com");
                                expect(resObj.supplier).to.equal("mock");

                                //End of test
                                done();
                            }
                        });

                    }
                })
            }

            let sql = "INSERT INTO laptop (brand, model, description, image_url, year, ram, ssd, cpu, screen) " +
                " VALUES ('" + tempBrand + "','" + tempModel + "', 'Mock test description', './mocktest.jpg', 9999, '99GB', '999GB', 'mock', '99-inch')";
            connectionPool.query(sql, (err, result) => {
                if (err) {//Check for errors
                    assert.fail(err);//Fail test if this does not work.
                    done();//End test
                }
                else {
                    sql = "SELECT id, brand, model FROM laptop WHERE brand='" + tempBrand + "' AND model='" + tempModel + "'";
                    connectionPool.query(sql, (err, result) => {
                        if (err) {//Check for errors
                            assert.fail(err);//Fail test if this does not work.
                            done();//End test
                        } else {
                            //Check that laptop has been added to database
                            expect(result.length).to.equal(1);
                            const id = result[0].id;

                            sql = "INSERT INTO comparison (laptop_id, price, url, supplier) " +
                                ` VALUES (${id}, 0000, 'mockExample.com', 'mock')`;
                            connectionPool.query(sql, (err, result) => {
                                if (err) {//Check for errors
                                    assert.fail(err);//Fail test if this does not work.
                                    done();//End test
                                } else {

                                    //Call function that we are testing
                                    db.getComparison(response, id);

                                }
                            });
                        }
                    });
                }
            });

        });
    });

    // //Mocha test for getAllCustomers method in database module.
    describe('#getLaptopsByParameters', () => {
        it('should GET laptops that have the same brand or model as in parameters', (done) => {
            //Mock response object for test
            let response = {};

            //Create random laptop details
            const tempBrand = Math.random().toString(36).substring(2, 15);
            const tempModel = Math.random().toString(36).substring(2, 15);


            /* When there is an error response.staus(ERROR_CODE).json(ERROR_MESSAGE) is called
               Mock object should fail test in this situation. */
            response.status = (errorCode) => {
                return {
                    json: (errorMessage) => {
                        console.log("Error code: " + errorCode + "; Error message: " + errorMessage);
                        assert.fail("Error code: " + errorCode + "; Error message: " + errorMessage);
                        done();
                    }
                }
            };

            //Add send function to mock object. This checks whether function is behaving correctly
            response.json = (res) => {

                //Clean up database
                sql = "DELETE FROM comparison WHERE price=" + 0;
                connectionPool.query(sql, (err, result) => {
                    if (err) {//Check for errors
                        assert.fail(err);//Fail test if this does not work.
                        done();//End test
                    } else {
                        sql = "DELETE FROM laptop WHERE brand='" + tempBrand + "' AND model='" + tempModel + "'";
                        connectionPool.query(sql, (err, result) => {
                            if (err) {//Check for errors
                                assert.fail(err);//Fail test if this does not work.
                                done();//End test
                            }
                            else {
                                // Convert result to JavaScript object
                                const resObj = res;

                                // Check that appropriate properties are returned
                                resObj.should.have.property('totalNumberOfLaptops');
                                resObj.should.have.property('data');

                                // Check that an array of laptops is returned
                                resObj.data.should.be.a('array');

                                // Check that the count of laptops is returned
                                resObj.totalNumberOfLaptops.should.be.a('number');

                                // Check that an array of laptpos is returned
                                resObj.data.should.be.a('array').with.lengthOf(resObj.totalNumberOfLaptops);

                                // Check that appropriate properties contain in data property array
                                if (resObj.data.length > 0) {
                                    resObj.data[0].should.have.property('id');
                                    resObj.data[0].should.have.property('brand');
                                    resObj.data[0].should.have.property('model');
                                    resObj.data[0].should.have.property('description');
                                    resObj.data[0].should.have.property('image_url');
                                    resObj.data[0].should.have.property('year');
                                    resObj.data[0].should.have.property('ram');
                                    resObj.data[0].should.have.property('ssd');
                                    resObj.data[0].should.have.property('cpu');
                                    resObj.data[0].should.have.property('screen');
                                }
                                // End of test
                                done();
                            }
                        });

                    }
                })

            };


            let sql = "INSERT INTO laptop (brand, model, description, image_url, year, ram, ssd, cpu, screen) " +
                " VALUES ('" + tempBrand + "','" + tempModel + "', 'Mock test description', './mocktest.jpg', 9999, '99GB', '999GB', 'mock', '99-inch')";
            connectionPool.query(sql, (err, result) => {
                if (err) {//Check for errors
                    assert.fail(err);//Fail test if this does not work.
                    done();//End test
                }
                else {
                    sql = "SELECT id, brand, model FROM laptop WHERE brand='" + tempBrand + "' AND model='" + tempModel + "'";
                    connectionPool.query(sql, (err, result) => {
                        if (err) {//Check for errors
                            assert.fail(err);//Fail test if this does not work.
                            done();//End test
                        } else {
                            //Check that laptop has been added to database
                            expect(result.length).to.equal(1);

                            sql = "INSERT INTO comparison (laptop_id, price, url, supplier) " +
                                ` VALUES (${result[0].id}, 0000, 'mockExample.com', 'mock')`;

                            connectionPool.query(sql, (err, result) => {
                                if (err) {//Check for errors
                                    assert.fail(err);//Fail test if this does not work.
                                    done();//End test
                                } else {

                                }
                            });
                        }
                    });
                }
            });

            //Call function to search laptop in database
            db.getLaptopsByParameters(response, tempBrand, tempModel, 9, 0);
        });
    });
});

//Wrapper for all web service tests
describe('Web Service', () => {
    //Test of GET request sent to /laptops
    describe('/GET compare/*', () => {
        it('should GET comparisons with specific laptop id', (done) => {
            chai.request(server)
                .get('/compare/351')
                .end((err, response) => {
                    //Check the status code
                    response.should.have.status(HTTP_STATUS.OK);

                    //Convert returned JSON to JavaScript object
                    const resObj = JSON.parse(response.text);

                    //Check that an array of laptpos is returned
                    resObj.should.be.a('array');

                    //Check that appropriate properties contain in data property array
                    if (resObj.length > 0) {
                        resObj[0].should.have.property('id');
                        resObj[0].should.have.property('laptop_id');
                        resObj[0].should.have.property('price');
                        resObj[0].should.have.property('url');
                        resObj[0].should.have.property('supplier');
                    }
                    //End test
                    done();
                });
        });
    });


    //Test of GET request sent to /laptops
    describe('/GET search/', () => {
        it('should GET laptops that have the same brand or model from url query', (done) => {
            chai.request(server)
                .get('/search?brand=apple&model=macbook pro&num_items=9&page=0')
                .end((err, response) => {
                    //Check the status code
                    response.should.have.status(HTTP_STATUS.OK);

                    //Convert returned JSON to JavaScript object
                    const resObj = JSON.parse(response.text);

                    //Check that appropriate properties are returned
                    resObj.should.have.property('totalNumberOfLaptops');
                    resObj.should.have.property('data');

                    //Check that the count of laptops is returned
                    resObj.totalNumberOfLaptops.should.be.a('number');

                    //Check that an array of laptpos is returned
                    resObj.data.should.be.a('array').with.lengthOf(resObj.totalNumberOfLaptops);

                    //Check that appropriate properties contain in data property array
                    if (resObj.data.length > 0) {
                        resObj.data[0].should.have.property('id');
                        resObj.data[0].should.have.property('brand');
                        resObj.data[0].should.have.property('model');
                        resObj.data[0].should.have.property('description');
                        resObj.data[0].should.have.property('image_url');
                        resObj.data[0].should.have.property('year');
                        resObj.data[0].should.have.property('ram');
                        resObj.data[0].should.have.property('ssd');
                        resObj.data[0].should.have.property('cpu');
                        resObj.data[0].should.have.property('screen');
                    }
                    //End test
                    done();
                });
        });
    });
});