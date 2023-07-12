package subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestAssuredUtils {

    public static ExtractableResponse<Response> create(RestAssuredCondition condition) {
        return RestAssured.given().log().all()
                .body(condition.getParams())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(condition.getPath())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> inquiry(RestAssuredCondition condition) {
        return RestAssured.given().log().all()
                .when().get(condition.getPath())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(RestAssuredCondition condition) {
        return RestAssured.given().log().all()
                .when().delete(condition.getPath())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> update(RestAssuredCondition condition) {
        return RestAssured.given().log().all()
                .body(condition.getParams())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(condition.getPath())
                .then().log().all()
                .extract();
    }
}
