package nextstep.subway.acceptance.line;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.acceptance.AbstractStep;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.model.Distance;

@Service
public class LineStep extends AbstractStep {
    private static final String REQUEST_URL = "/lines";
    private static final String SPECIFIC_URL_FORMAT = "/lines/%d";
    public static final String DUMMY_NAME = "1호선";
    public static final String COLOR = "bg-red-600";
    public static final Distance DUMMY_DISTANCE = new Distance(100);
    public static final Long DUMMY_UP_STATION_ID = 1L;
    public static final Long DUMMY_DOWN_STATION_ID = 2L;

    protected LineStep() {
        super(REQUEST_URL);
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청(final LineRequest params) {
        RequestSpecification given =
            RestAssured.given().log().all()
                       .body(params)
                       .contentType(MediaType.APPLICATION_JSON_VALUE);
        return request(given, Method.POST, REQUEST_URL);
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청(String name, long upStationId, long downStationId) {
        LineRequest params = dummyParams();
        params.setName(name);
        params.setUpStationId(upStationId);
        params.setDownStationId(downStationId);
        return 지하철_노선_생성_요청(params);
    }

    public ExtractableResponse<Response> 지하철_노선_수정_요청(long lineId, final LineRequest request) {
        RequestSpecification given =
            RestAssured.given().log().all()
                       .body(request)
                       .contentType(MediaType.APPLICATION_JSON_VALUE);
        return request(given, Method.PUT, specificRequestUrl(lineId));
    }

    public ExtractableResponse<Response> 지하철_노선_조회_요청(long lineId) {
        return request(Method.GET, specificRequestUrl(lineId));
    }

    public ExtractableResponse<Response> 지하철_노선_삭제_요청(long lineId) {
        return request(Method.DELETE, specificRequestUrl(lineId));
    }

    private String specificRequestUrl(long lineId) {
        return String.format(SPECIFIC_URL_FORMAT, lineId);
    }

    public LineRequest dummyParams() {
        return LineRequest.builder()
            .name(DUMMY_NAME)
            .color(COLOR)
            .upStationId(DUMMY_UP_STATION_ID)
            .downStationId(DUMMY_DOWN_STATION_ID)
            .distance(DUMMY_DISTANCE)
            .build();
    }
}
