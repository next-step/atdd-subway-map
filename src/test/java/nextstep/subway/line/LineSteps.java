package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.station.StationSteps.requestCreateStationGangnam;
import static nextstep.subway.station.StationSteps.requestCreateStationPangyo;
import static nextstep.subway.station.StationSteps.requestCreateStationSadang;
import static nextstep.subway.station.StationSteps.requestCreateStationYeoksam;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    private static final String URI_LINES = "/lines";
    private static final String HEADER_LOCATION = "Location";

    public static ExtractableResponse<Response> requestCreateLineDx() {
        ExtractableResponse<Response> createResponse1 = requestCreateStationGangnam();
        ExtractableResponse<Response> createResponse2 = requestCreateStationPangyo();

        Map<String, Object> params = makeLineParams(
                "bg-red-600",
                "신분당선",
                createResponse1.jsonPath().getLong("id"),
                createResponse2.jsonPath().getLong("id"),
                100000
        );
        return requestCreateLine(params);
    }

    public static ExtractableResponse<Response> requestCreateLineDxAgain() {
        ExtractableResponse<Response> createResponse1 = requestCreateStationYeoksam();
        ExtractableResponse<Response> createResponse2 = requestCreateStationSadang();

        Map<String, Object> params = makeLineParams(
                "bg-red-600",
                "신분당선",
                createResponse1.jsonPath().getLong("id"),
                createResponse2.jsonPath().getLong("id"),
                100000
        );
        return requestCreateLine(params);
    }

    public static ExtractableResponse<Response> requestCreateLine2() {
        ExtractableResponse<Response> createResponse1 = requestCreateStationYeoksam();
        ExtractableResponse<Response> createResponse2 = requestCreateStationSadang();

        Map<String, Object> params = makeLineParams(
                "bg-green-600",
                "2호선",
                createResponse1.jsonPath().getLong("id"),
                createResponse2.jsonPath().getLong("id"),
                150000
        );
        return requestCreateLine(params);
    }

    public static ExtractableResponse<Response> requestCreateLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(URI_LINES)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestGetLines() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(URI_LINES)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestGetLine(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header(HEADER_LOCATION);
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestUpdateLine(ExtractableResponse<Response> createResponse) {
        Map<String, Object> params = makeLineParams(
                "bg-blue-600",
                "구분당선",
                createResponse.jsonPath().getLong("downStationId"),
                createResponse.jsonPath().getLong("upStationId"),
                200000
        );
        String uri = createResponse.header(HEADER_LOCATION);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestDeleteLine(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header(HEADER_LOCATION);
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void assertCreateLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HEADER_LOCATION)).isNotBlank();
    }

    public static void assertCreateLineFail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void assertGetLines(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @SafeVarargs
    public static void assertIncludeLines(ExtractableResponse<Response> response, ExtractableResponse<Response>... createResponses) {
        List<Long> expectedIds = Stream.of(createResponses)
                .map(it -> Long.parseLong(it.header(HEADER_LOCATION).split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultIds).containsAll(expectedIds);
    }

    public static void assertGetLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void assertUpdateLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void assertDeleteLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static Map<String, Object> makeLineParams(String color, String name, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("color", color);
        params.put("name", name);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }
}
