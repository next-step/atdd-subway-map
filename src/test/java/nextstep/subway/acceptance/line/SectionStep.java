package nextstep.subway.acceptance.line;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Distance;

public class SectionStep {
    private static final long START_UP_STATION_NEW_SECTION = 2;
    private static final long START_DOWN_STATION_NEW_SECTION = 3;

    public ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, SectionRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines/" + lineId + "/sections")
                          .then().log().all()
                          .extract();
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, Long upStationId, Long downStationId) {
        SectionRequest sectionRequest = dummyRequest();
        sectionRequest.setUpStationId(upStationId);
        sectionRequest.setDownStationId(downStationId);
        return 지하철_구간_생성_요청(lineId, sectionRequest);
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId) {
        return 지하철_구간_생성_요청(lineId, dummyRequest());
    }

    public SectionRequest dummyRequest() {
        return SectionRequest.builder()
            .upStationId(START_UP_STATION_NEW_SECTION)
            .downStationId(START_DOWN_STATION_NEW_SECTION)
            .distance(new Distance(100))
            .build();
    }
}
