package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.section.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionAcceptanceRequest {
    private static String SECTION_PATH = "/lines/%d/sections";

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post(String.format(SECTION_PATH, lineId))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete(String.format(SECTION_PATH + "?stationId=%d", lineId, stationId))
                .then().log().all().extract();
    }
}
