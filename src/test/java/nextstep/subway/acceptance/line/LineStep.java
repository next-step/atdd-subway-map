package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.model.Distance;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class LineStep {
    public static final String DUMMY_NAME = "1호선";
    public static final String COLOR = "bg-red-600";
    public static final Distance DUMMY_DISTANCE = new Distance(100);
    public static final Long DUMMY_UP_STATION_ID = 1L;
    public static final Long DUMMY_DOWN_STATION_ID = 2L;

    public ExtractableResponse<Response> 지하철_노선_생성_요청(final LineRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines")
                          .then().log().all()
                          .extract();
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청(String name, long upStationId, long downStationId) {
        LineRequest request = dummyRequest();
        request.setName(name);
        request.setUpStationId(upStationId);
        request.setDownStationId(downStationId);
        return 지하철_노선_생성_요청(request);
    }

    public LineRequest dummyRequest() {
        return LineRequest.builder()
            .name(DUMMY_NAME)
            .color(COLOR)
            .upStationId(DUMMY_UP_STATION_ID)
            .downStationId(DUMMY_DOWN_STATION_ID)
            .distance(DUMMY_DISTANCE)
            .build();
    }
}
