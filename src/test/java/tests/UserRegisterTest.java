package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestcase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.Assertions.*;

public class UserRegisterTest extends BaseTestcase {

    @Test
    public void testCreateUserWithExistingEmail () {

        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "aez");
        userData.put("lastName", "aez");

        Response responceCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        assertResponseTextEquals(responceCreateAuth, "Users with email '" + email +"' already exists");
        assertResponseCodeEquals(responceCreateAuth, 400);
    }

    @Test
    public void testCreateUserSuccessfully () {

        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "aez");
        userData.put("lastName", "aez");

        Response responceCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        assertResponseCodeEquals(responceCreateAuth, 200);
        assertJsonHasField(responceCreateAuth, "id");
    }
}
