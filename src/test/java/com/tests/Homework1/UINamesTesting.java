package com.tests.Homework1;


import com.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
;

public class UINamesTesting {

    @BeforeAll
    public static void setup()
    {
        baseURI = ConfigurationReader.getProperty("uinames.uri");
    }

    /**
     * No params test
     * 1. Send a get request without providing any parameters
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Verify that name, surname, gender, region fields have value
     */

    @Test
    @DisplayName("Verify that name, surname, gender, region fields have value")
    public void noparameter() {
        given().get().
                then().assertThat().statusCode(200).
                contentType("application/json; charset=utf-8").
                body("name", notNullValue()).
                body("surname", notNullValue()).
                body("gender", notNullValue()).
                body("region", notNullValue()).
                log().all(true);
    }

    @Test
    public void noParamTest() {
        Response response = given().accept(ContentType.JSON).
                get();
        response.then().
                assertThat().statusCode(200).
                contentType("application/json; charset=utf-8");
        JsonPath json = response.thenReturn().jsonPath();

        Map<String, String> map = json.get();
        for (Map.Entry entry : map.entrySet()) {
            assertTrue((entry.getKey() != (null)));


        }
    }

    /**
     * 1.Create a request by providing query parameter: gender, male or female
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that value of gender field is same from step 1
     */
    @Test
    @DisplayName("Verify that value of gender field is same from step 1")
    public void test2() {
        given().queryParam("gender", "male").
                when().get().then().assertThat().statusCode(200).
                contentType("application/json; charset=utf-8").

                body("{gender}", notNullValue()).log().all(true);

    }

    /**
     * 1.Create a request by providing query parameters:
     * a valid region and gender NOTE: Available region values are given in the documentation
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that value of gender field is same from step 1
     * 4.Verify that value of region field is same from step 1
     */

    @Test
    @DisplayName("Verify that value of gender field is same from step 1")
    public void test3() {
        given().queryParam("region", "Slovakia").
                queryParam("gender", "male").
                when().get().then().assertThat().
                contentType("application/json; charset=utf-8").assertThat().statusCode(200).
                body("region", is("Slovakia")).
                body("gender", is("male")).log().all(true);


    }

    /**
     * Invalid gender test
     * 1.Create a request by providing query parameter: invalid gender
     * 2.Verify status code 400 and status line contains Bad Request
     * 3.Verify that value of error field is Invalid gender
     */

    @Test
    @DisplayName("Verify that value of error field is Invalid gender")
    public void test4() {
        given().queryParam("gender", "mustafa").
                when().get().
                then().assertThat().statusCode(400).statusLine("HTTP/1.1 400 Bad Request").
                contentType("application/json; charset=utf-8").
                body(hasValue("Invalid gender")).log().all(true);
// body("error", containsString("Invalid gender")).
//                log().all(true);
    }

    /**
     * Invalid region test
     * 1.Create a request by providing query parameter: invalid region
     * 2.Verify status code 400 and status line contains Bad Request
     * 3.Verify that value of error field is Region or language not found
     */

    @Test
    @DisplayName("Verify that value of error field is Invalid gender")
    public void test5() {

        given().queryParam("region", "Konya").get().
                then().assertThat().statusCode(400).
                contentType("application/json; charset=utf-8").
                statusLine("HTTP/1.1 400 Bad Request").body("error", is("Region or language not found"))
                .log().all(true);                             //containsString("Region or language not found")).


    }

    /**
     * Amount and regions test
     * 1.Create request by providing query parameters:
     * a valid region and amount(must be bigger than 1)
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that all objects have different name+surname combination
     */

    @Test
    @DisplayName("Verify that all objects have different combination")
    public void test6() {
        Response response = given().queryParam("region", "USA").
                queryParam("amount", "5").
                when().get();
        response.then().assertThat().statusCode(200).
                contentType("application/json; charset=utf-8").log().all(true);
//3.Verify that all objects have different name+surname combination
    }

    @Test
    @DisplayName("Verify that all objects same different combination")
    public void test7() {
        Response response = given().queryParam("region", "USA").
                queryParam("gender","female").
                queryParam("amount", "3").
                when().get();
        response.then().assertThat().statusCode(200).
                contentType("application/json; charset=utf-8").
                body("region", hasItem("USA")).
                body("gender", hasItem("female")).
                log().all(true);

    //body(hasProperty).


    }
/**

 Amount count test
 1.Create a request by providing query parameter: amount (must be bigger than 1)
 2.Verify status code 200, content type application/json; charset=utf-8
 3.Verify that number of objects returned in the response is same as the amount passed in step 1
*/

@Test
@DisplayName("Verify that all objects same different combination")
public void test8() {
    Response response = given().
            queryParam("amount", "7").
            when().get();//response.jsonPath().getList("amount");
   response. then().assertThat().statusCode(200).
            contentType("application/json; charset=utf-8").
            body("amount",hasSize(7)).log().all(true);


}
}