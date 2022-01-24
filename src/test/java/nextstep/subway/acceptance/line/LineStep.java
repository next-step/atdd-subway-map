package nextstep.subway.acceptance.line;

import java.util.Objects;
import java.util.function.Consumer;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.model.Distance;

public class LineStep {
    private static final String NAME_FORMAT = "%d호선";
    private static final String COLOR = "bg-red-600";

    private int dummyCounter = 0;
    private long stationIdCounter = 0;
    private final StationStep stationStep;

    public LineStep(StationStep stationStep) {
        this.stationStep = stationStep;
    }

    public LineStep() {
        this(new StationStep());
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청(final LineRequest request) {
        stationStep.지하철역_생성_요청();
        stationStep.지하철역_생성_요청();
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines")
                          .then().log().all()
                          .extract();
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청() {
        return 지하철_노선_생성_요청(dummyRequest());
    }

    public LineRequest dummyRequest() {
        return LineRequest.builder()
            .name(nextName())
            .color(nextColor())
            .upStationId(++stationIdCounter)
            .downStationId(++stationIdCounter)
            .distance(nextDistance())
            .build();
    }

    public synchronized String nextName() {
        return String.format(NAME_FORMAT, ++dummyCounter);
    }

    public String nextColor() {
        return COLOR;
    }

    private Distance nextDistance() {
        return new Distance(2);
    }
}
