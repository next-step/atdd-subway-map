package nextstep.subway.acceptance.line;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.acceptance.AbstractStep;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Distance;

@Service
public class SectionStep extends AbstractStep {
    private static final String REQUEST_URL = "/lines/%d/sections";
    private static final long START_UP_STATION_NEW_SECTION = 2;
    private static final long START_DOWN_STATION_NEW_SECTION = 3;

    protected SectionStep() {
        super(REQUEST_URL);
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청(long lineId, SectionRequest request) {
        RequestSpecification given =
            RestAssured.given().log().all()
                       .body(request)
                       .contentType(MediaType.APPLICATION_JSON_VALUE);
        return request(given, Method.POST, requestUrl(lineId));
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청(long lineId, long upStationId, long downStationId) {
        SectionRequest sectionRequest = dummyRequest();
        sectionRequest.setUpStationId(upStationId);
        sectionRequest.setDownStationId(downStationId);
        return 지하철_구간_생성_요청(lineId, sectionRequest);
    }

    public ExtractableResponse<Response> 지하철_구간_삭제_요청(long lineId, long sectionId) {
        String path = requestUrl(lineId) + "/" + sectionId;
        return request(Method.DELETE, path);
    }

    private String requestUrl(long lineId) {
        return String.format(REQUEST_URL, lineId);
    }

    public SectionRequest dummyRequest() {
        return SectionRequest.builder()
            .upStationId(START_UP_STATION_NEW_SECTION)
            .downStationId(START_DOWN_STATION_NEW_SECTION)
            .distance(new Distance(100))
            .build();
    }
}
