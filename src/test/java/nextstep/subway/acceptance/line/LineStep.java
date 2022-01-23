package nextstep.subway.acceptance.line;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.section.domain.model.Distance;

public class LineStep {
    private static int dummyCounter = 0;
    private static final String NAME_FORMAT = "%d호선";
    private static final String COLOR_FORMANT = "bg-red-%d";

    private LineStep() {}

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(final LineRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Distance distance) {
        LineRequest request = LineRequest.builder()
            .name(nextRequest())
            .color(nextColor())
            .upStationId((long) 1)
            .downStationId((long) 2)
            .distance(distance)
            .build();
        return 지하철_노선_생성_요청(request);
    }

    public static String nextRequest() {
        return String.format(NAME_FORMAT, ++dummyCounter);
    }

    public static String nextColor() {
        return String.format(COLOR_FORMANT, ++dummyCounter);
    }
}
