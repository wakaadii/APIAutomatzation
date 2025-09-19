package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestcase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.Assertions.*;

public class UserGetTest extends BaseTestcase {

    @Test
    public void testGetUserDataNotAuth() {
        Response responceNonAuth = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        assertJsonHasField(responceNonAuth, "username");
        assertJsonHasNotField(responceNonAuth, "firstName");
        assertJsonHasNotField(responceNonAuth, "lastName");
        assertJsonHasNotField(responceNonAuth, "email");
        System.out.println(responceNonAuth.asString());
    }

    @Test
    public void testGetUserDataAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responceGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responceGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responceGetAuth, "auth_sid");

        Response responceUserData = RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        assertJsonHasField(responceUserData, "username");
        assertJsonHasField(responceUserData, "firstName");
        assertJsonHasField(responceUserData, "lastName");
        assertJsonHasField(responceUserData, "email");
        System.out.println(responceUserData.asString());

    }

    @Test
    public void testUserAuthMultipleCheck() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responceGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responceGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responceGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        assertJsonHasSomeFields(responseUserData, new String[]{"username", "firstName", "lastName", "email"});
    }
}
