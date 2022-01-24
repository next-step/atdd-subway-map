package nextstep.subway.acceptance.line;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Distance;

public class SectionStep {
    private int dummyStation = 0;

    public ExtractableResponse<Response> 지하철_구간_생성_요청(SectionRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines/1/sections")
                          .then()
                          .log().all()
                          .extract();
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청(Long upStationId, Long downStationId) {
        return 지하철_구간_생성_요청(dummyRequest(upStationId, downStationId));
    }

    public SectionRequest dummyRequest(Long upStationId, Long downStationId) {
        return SectionRequest.builder()
            .upStationId(upStationId)
            .downStationId(downStationId)
            .distance(new Distance(100))
            .build();
    }
}
