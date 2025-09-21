package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestcase;
import lib.Assertions;
import lib.APICoreRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import io.qameta.allure.Description;



@Epic("auth cases")
@Feature("Auth")
public class UserAuthTest extends BaseTestcase {

    String cookie;
    String header;
    int userIdOnAuth;
    public final APICoreRequests apiCoreRequests = new APICoreRequests();

    @BeforeEach
    public void loginUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description("Positive auth by email & password")
    @DisplayName("Positive auth user")
    public void testAuthUser (){

        Response respohseCheckAuth = apiCoreRequests
                .makeGetRequests(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie);


        Assertions.assertJsonByName(respohseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @ParameterizedTest
    @ValueSource(strings =  {"cookie", "header"})
    @Description("Try to login without cookie or csrf-token")
    @DisplayName("Neagtive auth user test")
    public void testNegativeAuth(String condition) throws IllegalAccessException {

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestsWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("header")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestsWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalAccessException("condition value is known: " + condition);
        }



    }
}
