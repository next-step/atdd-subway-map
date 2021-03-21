package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.section.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionAcceptanceRequest {
    private static String getSectionPath(Long lineId) {
        return String.format("/lines/%d/sections", lineId);
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post(getSectionPath(lineId))
                .then().log().all().extract();
    }
}
