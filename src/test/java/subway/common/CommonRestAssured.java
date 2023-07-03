package subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class CommonRestAssured {

    private static final String SLASH = "/";

    public static ExtractableResponse<Response> create(CommonRestAssuredUseCondition condition) {
        return RestAssured.given().log().all()
                .body(condition.getParams())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(condition.getPath())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> inquriy(CommonRestAssuredUseCondition condition) {
        return RestAssured.given().log().all()
                .when().get(condition.getPath())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(CommonRestAssuredUseCondition condition) {
        return RestAssured.given().log().all()
                .when().delete(condition.getPath() + SLASH + condition.getId())
                .then().log().all()
                .extract();
    }

}
