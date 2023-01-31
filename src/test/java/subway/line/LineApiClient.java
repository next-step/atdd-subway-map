package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.section.SectionRequest;

public class LineApiClient {
    private static final String ENDPOINT_LINES = "/lines";
    private static final String ENDPOINT_SECTIONS = "/sections";

    public static ExtractableResponse<Response> requestCreateLine(String name,
                                                           String color,
                                                           Long upStationId,
                                                           Long downStationId,
                                                           Integer distance) {
        LineRequest lineRequest = LineRequest.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();

        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(ENDPOINT_LINES)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> requestShowLines() {
        return RestAssured.given().log().all()
                .when().get(ENDPOINT_LINES)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestShowLine(Long id) {
        final String ENDPOINT = ENDPOINT_LINES + "/" + id.toString();

        return RestAssured.given().log().all()
                .when().get(ENDPOINT)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> requestUpdateLine(Long id, String name, String color) {
        final String ENDPOINT = ENDPOINT_LINES + "/" + id.toString();

        LineRequest lineRequest = LineRequest.builder()
                .name(name)
                .color(color)
                .build();

        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(ENDPOINT)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> requestDeleteLine(Long id) {
        final String ENDPOINT = ENDPOINT_LINES + "/" + id.toString();

        return RestAssured.given().log().all()
                .when().delete(ENDPOINT)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestAppendSection(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        final String ENDPOINT = ENDPOINT_LINES + "/" + lineId.toString() + ENDPOINT_SECTIONS;

        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);

        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(ENDPOINT)
                .then().log().all()
                .extract();
    }
}
