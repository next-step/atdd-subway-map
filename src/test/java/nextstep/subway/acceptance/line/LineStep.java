package nextstep.subway.acceptance.line;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.model.Distance;

@Service
public class LineStep {
    private static final String NAME_FORMAT = "%d호선";
    private static final String COLOR = "bg-red-600";

    private int dummyCounter = 0;

    public ExtractableResponse<Response> 지하철_노선_생성_요청(final LineRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines")
                          .then().log().all()
                          .extract();
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청(long upStationId, long downStationId) {
        LineRequest request = dummyRequest();
        request.setUpStationId(upStationId);
        request.setDownStationId(downStationId);
        return 지하철_노선_생성_요청(request);
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청() {
        return 지하철_노선_생성_요청(dummyRequest());
    }

    public LineRequest dummyRequest() {
        return LineRequest.builder()
            .name(nextName())
            .color(nextColor())
            .upStationId((long) 1)
            .downStationId((long) 2)
            .distance(nextDistance())
            .build();
    }

    public String nextName() {
        return String.format(NAME_FORMAT, ++dummyCounter);
    }

    public String nextColor() {
        return COLOR;
    }

    private Distance nextDistance() {
        return new Distance(2);
    }
}
