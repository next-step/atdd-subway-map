package nextstep.subway.acceptance.line;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Distance;

public class SectionStep {
    private static final long UP_STATION_NEW_SECTION = 2;
    private static final long DOWN_STATION_NEW_SECTION = 3;

    private int dummyStation = 0;
    private final LineStep lineStep;

    public SectionStep(LineStep lineStep) {
        this.lineStep = lineStep;
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청(SectionRequest request) {
        lineStep.지하철_노선_생성_요청();
        lineStep.getStationStep().지하철역_생성_요청();

        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines/1/sections")
                          .then().log().all()
                          .extract();
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청() {
        return 지하철_구간_생성_요청(dummyRequest());
    }

    public SectionRequest dummyRequest() {
        return SectionRequest.builder()
            .upStationId(UP_STATION_NEW_SECTION)
            .downStationId(DOWN_STATION_NEW_SECTION)
            .distance(new Distance(100))
            .build();
    }
}
