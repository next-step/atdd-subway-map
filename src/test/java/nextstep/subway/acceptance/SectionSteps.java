package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.MediaType;

public class SectionSteps {

    private static final String SECTION_URI = "/lines/%d/sections";
    private static final int DEFAULT_DISTANCE = 10;

    public static ExtractableResponse<Response> 구간_생성_요청(SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format(SECTION_URI, sectionRequest.getLineId()))
                .then().log().all()
                .extract();
    }

    public static SectionRequest 신규_구간(LineResponse lineResponse, StationResponse upStation, StationResponse downStation) {
        return SectionRequest.of(lineResponse.getId(), upStation.getId(), downStation.getId(), DEFAULT_DISTANCE);
    }

    public static int 구간_생성_요청_응답_HttpStatusCode(SectionRequest sectionRequest) {
        ExtractableResponse<Response> response = SectionSteps.구간_생성_요청(sectionRequest);
        return response.statusCode();
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .queryParam("stationId", sectionRequest.getId())
                .when()
                .delete(String.format(SECTION_URI, sectionRequest.getLineId()))
                .then().log().all()
                .extract();
    }

    public static int 구간_삭제_요청_응답_HttpStatusCode(SectionRequest sectionRequest) {
        ExtractableResponse<Response> response = SectionSteps.구간_삭제_요청(sectionRequest);
        return response.statusCode();
    }

}
