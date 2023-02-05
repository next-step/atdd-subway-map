package subway.line.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.dto.request.LineRequest;
import subway.line.dto.request.SectionRequest;

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

    public static ExtractableResponse<Response> requestShowLines() {
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

    public static ExtractableResponse<Response> requestUpdateLine(Long id, String name, String color) {
        LineRequest lineRequest = LineRequest.builder()
                .name(name)
                .color(color)
                .build();

        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(ENDPOINT_LINES + "/{lineId}", id.toString())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestDeleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete(ENDPOINT_LINES + "/{lineId}", id.toString())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestAppendSection(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);

        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(ENDPOINT_LINES + "/{lineId}" + ENDPOINT_SECTIONS, lineId.toString())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestDeleteSection(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when()
                .delete(ENDPOINT_LINES + "/{lineId}" + ENDPOINT_SECTIONS + "?=stationId={stationId}",
                        lineId.toString(), stationId.toString())
                .then().log().all()
                .extract();
    }
}
