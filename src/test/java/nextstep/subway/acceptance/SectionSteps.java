package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionSteps {
    public static ExtractableResponse<Response> 지하철_구간_생성(SectionRequest sectionRequest, Long lineId) {
        return RestAssured.given().log().all()
            .body(sectionRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(String.format("/lines/%s/sections", lineId))
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(String.format("/lines/%s/sections?stationId=%s", lineId, stationId))
            .then().log().all()
            .extract();
    }
}
