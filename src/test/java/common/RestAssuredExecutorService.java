package common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.StationRequest;

public class RestAssuredExecutorService {

    public static ExtractableResponse<Response> getForResponse(String path) {
        return RestAssured
                .given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postForResponse(String path, StationRequest parameter) {
        return RestAssured
                .given().log().all()
                .body(parameter)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

}
