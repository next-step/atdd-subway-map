package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.StationSectionRequest;

import static io.restassured.RestAssured.given;

public class StationSectionSteps {

    public static ExtractableResponse<Response> 지하철_구간_생성요청(Long upStationId, StationSectionRequest request) {
        return given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", upStationId))
                .then().extract();
    }
}
