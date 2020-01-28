package com.tests.day3;


import com.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class ORDSTestsDay3 {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }
    //accept("application/json") shortcut for header("Accept", "application/json")
    //we are asking for json as a response
    @Test
    public void test1() {
        given().
                accept("application/json").
                get("/employees").
                then().
                assertThat().statusCode(200).
                and().assertThat().contentType("application/json").
                log().all(true);
    }
    //path parameter - to point on specific resource /employee/:id/ - id it's a path parameter
    //query parameter - to filter results, or describe new resource :
    // POST /users?name=James&age=60&job-title=SDET
    //or to filter GET /employee?name=Jamal get all employees with name Jamal
    @Test
    public void test2() {
        given().
                accept("application/json").
                pathParam("id", 100).
                when().get("/employees/{id}").
                then().assertThat().statusCode(200).
                and().assertThat().body("employee_id", is(100),
                "department_id", is(90),
                "last_name", is("King")).
                log().all(true);
        //body ("phone_number") --> 515.123.4567
        //is is coming from ---> import static org.hamcrest.Matchers.*;
        //log().all  Logs everything in the response, including e.g. headers,
        // cookies, body with the option to pretty-print the body if the content-type is
    }
    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id is equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */
@Test
public void test3(){
    given().accept("application/json").pathParam("id",1).
                                       when().get("/regions/{id}").
                                        then().assertThat().statusCode(200).
                                          assertThat().body("region_name",is("Europe")).
                                       time(lessThan(10L), TimeUnit.SECONDS).
                                        log().body(true);//log body in pretty format
                                             //    all =header+body+status code

//verify that responce time is more than 10 seconds
}
    /** ####TASK#####
     * Given accept type as JSON
     * And path parameter is id
     * When user sends get request to /locations
     *  Then user verifies that status code is 200
     *  And user verifies that location_id is 1700
     *  And user verifies that postal_code is 98199
     *  And user verifies that city is Seattle
     *  And user verifies that state_province is Washington
     */




    @Test
    public void test4() {
JsonPath json=given().accept("application/json").
        when().
        get("employees").
        thenReturn().jsonPath();
String nameOfEmployee=json.getString("items[0].first_name");//first one
        System.out.println("First EmployeeName = "+nameOfEmployee);
   String nameOfLastEmployees=json.getString("items[-1].first_name");
        System.out.println("Last EmployeeName = "+nameOfLastEmployees);
        Map<String,?>firstEmployee=json.get("items[0]");//we put ? bcs it can be also not string
        System.out.println(firstEmployee);//brings us all items[0]

        //since first employee it is a map(key-value pair we can iterate through it by using Entry.entry represent one key=value pair)
for(Map.Entry<String,?>entry:firstEmployee.entrySet()){
    System.out.println("key: "+entry.getKey()+",value: "+entry.getValue());
}
     //   List<String> lastEmployee=json.getString("items[-1]");


    }
//write a code get info from countries as List<Map<String,?>>
    @Test
public void test5(){
        JsonPath json= //optional
                given().accept("application/json").
                when().get("/countries").prettyPeek().
                        jsonPath();

    String nameOfCountries= json.getString("items[0].country_name");
        System.out.println(nameOfCountries);
Map<String,?>countryInfo=json.getMap("items[0]");
        System.out.println(countryInfo);
        List<Map<String,?>>allCountries=json.get("items");
        System.out.println(allCountries);
    }

//get collection of salaries
//then sort it
    //and print
@Test
public void test6(){
List<Integer> salaries=given().accept("application/json").
        when().get("/employees").thenReturn().jsonPath().get("items.salary");
    Collections.sort(salaries);//sort from a-z 0-9
    Collections.reverse(salaries);
    System.out.println(salaries);
    }
//get collection of phone numbers from employees
//and replace all dots "." in every phone

@Test
    public void test7(){
 List<String> phonenumber=  given().accept("application/json").
    when().get("/employees").thenReturn().jsonPath().
            get("items.phone_number");

    System.out.println(phonenumber);
    System.out.println("=========================");

    //for replace "."
  phonenumber.replaceAll(phone->phone.replace(".","_"));//lambda to java 8
    System.out.println(phonenumber);

    }
@Test
public void test8() {

    Response response = given().accept(ContentType.JSON).
            pathParam("id", 1700).
           when().get("/locations/{id}");

    response.then().assertThat().body("location_id", is(1700)).
            assertThat().body("postal_code", is("98199")).
            assertThat().body("city", is("Seattle")).
            assertThat().body("state_province", is("Washington")).
            log().body();

}










}
