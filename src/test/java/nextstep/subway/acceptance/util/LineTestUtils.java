package nextstep.subway.acceptance.util;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import org.springframework.http.MediaType;

public class LineTestUtils {

    public static ExtractableResponse<Response> createLine(LineCreationRequest creationRequest) {
        return RestAssured
                .given()
                    .body(creationRequest, ObjectMapperType.JACKSON_2)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .extract();
    }

    public static ExtractableResponse<Response> getLine(Long lineId) {
        return RestAssured
                .given()
                    .pathParam("lineId", lineId)
                .when()
                    .get("/lines/{lineId}")
                .then()
                    .extract();
    }

    public static ExtractableResponse<Response> getAllLines() {
        return RestAssured
                .when()
                    .get("/lines")
                .then()
                    .extract();
    }
}
