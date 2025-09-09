package lib;

import io.restassured.response.Response;
import io.restassured.http.Headers;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestcase {
    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response hasn't header with name \"" + name +"\"");
        return headers.getValue(name);
    }

    protected String getCookie(Response Responce, String name) {
        Map<String, String> cookies = Responce.getCookies();

        assertTrue(cookies.containsKey(name), "Response hasn't cookie with name \"" + name +"\"");
        return cookies.get(name);
    }

    protected int getIntFromJson(Response Responce, String name) {
        Responce.then().assertThat().body("$", hasKey(name));
        return Responce.jsonPath().getInt(name);
    }
}
