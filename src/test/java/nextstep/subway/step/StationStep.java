package nextstep.subway.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationRequest;
import org.springframework.http.MediaType;

public class StationStep {

    private static final String PATH = "/stations";

    public static ExtractableResponse<Response> 역_생성(String stationName) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new StationRequest(stationName))
            .when()
            .post(PATH)
            .then()
            .extract();
    }

    public static ExtractableResponse<Response> 역_조회() {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(PATH)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 역_삭제(String path) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(path)
            .then().log().all()
            .extract();
    }

}
