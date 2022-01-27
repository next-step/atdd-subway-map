package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionSteps {

    private static final String SECTION_URI = "/lines/%d/sections";
    private static final int DEFAULT_DISTANCE = 10;

    public static ExtractableResponse<Response> 구간_생성_요청(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format(SECTION_URI, lineId))
                .then().log().all()
                .extract();
    }

    public static SectionRequest 신규_구간(LineResponse lineResponse, StationResponse upStation, StationResponse downStation) {
        return SectionRequest.of(lineResponse.getId(), upStation.getId(), downStation.getId(), DEFAULT_DISTANCE);
    }

    public static int 구간_생성_요청_응답_HttpStatusCode(LineResponse lineResponse, SectionRequest sectionRequest) {
        ExtractableResponse<Response> response = SectionSteps.구간_생성_요청(lineResponse.getId(), sectionRequest);
        return response.statusCode();
    }
}
