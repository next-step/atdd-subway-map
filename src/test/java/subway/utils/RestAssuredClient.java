package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.Map;
import org.springframework.http.MediaType;

public class RestAssuredClient {

    public static ValidatableResponse requestPost(String path, Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(path)
            .then().log().all();
    }

    public static ValidatableResponse requestGet(String path) {
        return RestAssured.given().log().all()
            .when().get(path)
            .then().log().all();
    }

}
