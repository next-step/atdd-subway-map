package nextstep.subway.acceptance.station;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.acceptance.AbstractStep;
import nextstep.subway.station.domain.dto.StationRequest;

@Service
public class StationStep extends AbstractStep {
    private static final String REQUEST_URL = "/stations";
    private static final String SPECIFIC_URL_FORMAT = "/stations/%d";
    public static final String DUMMY_STATION_NAME = "1호선";

    protected StationStep() {
        super(REQUEST_URL);
    }

    public ExtractableResponse<Response> 지하철역_생성_요청(final String name) {
        StationRequest params = new StationRequest(name);
        RequestSpecification given =
            RestAssured.given().log().all()
                       .body(params)
                       .contentType(MediaType.APPLICATION_JSON_VALUE);
        return request(given, Method.POST, REQUEST_URL);
    }

    public ExtractableResponse<Response> 지하철_역_조회_요청() {
        return request(Method.GET, REQUEST_URL);
    }

    public ExtractableResponse<Response> 지하철_역_삭제_요청(long lineId) {
        return request(Method.DELETE, specificRequestUrl(lineId));
    }

    private String specificRequestUrl(long lineId) {
        return String.format(SPECIFIC_URL_FORMAT, lineId);
    }
}
