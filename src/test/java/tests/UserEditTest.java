package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestcase;
import org.junit.jupiter.api.Test;

import static lib.Assertions.assertJsonByName;
import static lib.DataGenerator.getRegistrationData;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestcase {
    @Test
    public void testEditJustCreatedUser() {
        //Generate User
        Map<String, String> userData = getRegistrationData();

        JsonPath responceCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userID = responceCreateAuth.getString("id");

        //login

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responceGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //Edit
        String newName = "changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responcenewEditUSer = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responceGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responceGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userID)
                .andReturn();

        //Check
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responceGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responceGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userID)
                .andReturn();

        assertJsonByName(responseUserData, "firstName", newName);
    }
}
