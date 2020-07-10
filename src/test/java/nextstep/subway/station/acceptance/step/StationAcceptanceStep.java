package nextstep.subway.station.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceStep {
    public static ExtractableResponse<Response> 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/stations").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured.given().log().all().
                when().
                get("/stations").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given().log().all().
                when().
                delete(uri).
                then().
                log().all().
                extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        return RestAssured.given().log().all().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(params).
            when().
            post("/lines").
            then().
            log().all().
            extract();
    }

    public static ExtractableResponse<Response> 노선에_지하철역_첫번째_등록(Long givenStationId, Long lineId) {
        Map<String, String> givenParams = new HashMap<>();
        givenParams.put("preStationId", "");
        givenParams.put("stationId", givenStationId + "");
        givenParams.put("distance", "4");
        givenParams.put("duration", "2");

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(givenParams)
            .when()
            .post("/lines/{lineId}/stations", lineId)
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선에_지하철역_추가로_등록(Long preStationId, Long givenStationId, Long lineId) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId + "");
        params.put("stationId", givenStationId + "");
        params.put("distance", "4");
        params.put("duration", "2");

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .post("/lines/{lineId}/stations", lineId)
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선정보_확인_요청(Long lineId) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/lines/{lineId}", lineId)
            .then()
            .log()
            .all()
            .extract();
    }
}
