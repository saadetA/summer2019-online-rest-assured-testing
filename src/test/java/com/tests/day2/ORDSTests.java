package com.tests.day2;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;


import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class ORDSTests {
    //address of ORDS web service, that is running no AWS ec2.
    //data is coming from SQL Oracle data base to this web service
    //during back-end testing with SQL developer and JDBC API
    //we were accessing data base directly
    //now, we gonna access web service
    private String baseURI = "http://ec2-3-84-250-120.compute-1.amazonaws.com:1000/ords/hr";
    //verify that status code is 200
    @Test      //ec2-18-207-188-234.compute-1.amazonaws.com
    public void test1() {
        Response response = given().get(baseURI + "/employees");
        System.out.println(response.getBody().asString());
        assertEquals(200, response.getStatusCode());
    System.out.println(response.prettyPrint());

    }
 //Task:get employee with id 100 and verify that response returns status code 200
   //also print body
    @Test
public void test2(){
        //header stands for meta data
        //usually it's used to include cookies
        //in this example, we are specifying what kind of response type we need
        //because web service can return let's say json or xml
        //when we put header info "Accept", "application/json", we are saying that we need only json as response

 //  ====================================

        //  Response response=given().get( baseURI + "/employees/100") ; //path parameter or
        Response response=given().header("accept","application/json").get( baseURI + "/employees/100") ;
        int actualStatusCode=response.statusCode();
   System.out.println(response.prettyPrint());
   assertEquals(200,actualStatusCode);

    }
//#Task: perform GET request to /regions, print body and all headers.
    @Test
   public  void test3() {
        Response response = given().get(baseURI + "/regions");
       // System.out.println(response.getBody().asString());
        assertEquals(200, response.getStatusCode());
        Header header=response.getHeaders().get("Content-Type");
        System.out.println(header);
       // System.out.println(response.prettyPrint());
//print all headers one by one

        for(Header h: response.getHeaders()){
            System.out.println(h);
        }
        System.out.println("============================");
        System.out.println(response.prettyPrint());
    }

}
