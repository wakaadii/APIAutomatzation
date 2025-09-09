package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {




    @Test
    public void testHelloWorld() {
        Response responce = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        responce.prettyPrint();
    }

    @Test
    public void testRestAssured () {
        String answer = "answer1";
        Map<String,Object> body = new HashMap();
        body.put("param1","value1");
        body.put("param2","value2");

        
        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        String name = response.print();


        if (name == null){
            System.out.println("The key " + answer + " is absent");
        } else  {
            System.out.println(name);
        }

    }


    @Test
    public void testResponseCode () {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        int statusCode = response.statusCode();
        System.out.println(statusCode);

    }
    @Test
    public void test303code () {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        int statusCode = response.statusCode();
        System.out.println(statusCode);
        response.prettyPrint();
    }

    @Test
    public void testHeaders () {
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");


        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint();


        String locationHeader = response.getHeader("Location");
        System.out.println();
        System.out.println(locationHeader);

        Headers responceHeaders = response.getHeaders();
        System.out.println();
        System.out.println(responceHeaders);
    }

    @Test
    public void testCookie (){
        Map<String,String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text");
        responseForGet.prettyPrint();

        String authCookie = responseForGet.getCookie("auth_cookie");

        Map <String, String> cookies = new HashMap<>();
        if (authCookie != null) {
            cookies.put("auth_cookie", authCookie);
        } else {
            System.out.println("\nAuth error");
            return;
        }

        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();

        /*System.out.println("\nheaders");
        Headers responceHeaders = responseForGet.getHeaders();
        System.out.println(responceHeaders);

        Map<String,String> cookie = responseForGet.getCookies();

        System.out.println(authCookie);
        System.out.println(cookie);*/
    }

    @Test
    public void testAssertionEquals () {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200, response.statusCode(),  "unexpected status code");
    }

    @Test
    public void testFor404 () {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/mapadf")
                .andReturn();
        assertEquals(404, response.statusCode(),  "unexpected status code");
    }

    @ParameterizedTest
    @ValueSource(strings =  {"", "ilya", "username"})
    public void testHelloWithoutName (String name) {
        Map<String,String> queryParams = new HashMap<>();

        if (!name.isEmpty()) {
            queryParams.put("name", name);
        }
        JsonPath responce = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = responce.getString("answer");
        String expectedAnswer = (!name.isEmpty()) ? name : "someone";
        assertEquals("Hello, " + expectedAnswer, answer, "unexpected answer");
    }
}
