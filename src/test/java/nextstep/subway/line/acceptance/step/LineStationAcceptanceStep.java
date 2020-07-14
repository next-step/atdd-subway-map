package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceStep {
    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(Long lineId, Long preStationId, Long stationId1, String distance, String duration) {
        Map<String, String> params = createLineStationRequestParams(preStationId, stationId1, distance, duration);

        return createLineStation(params, lineId);
    }

    public static ExtractableResponse<Response> createLineStation(Map<String, String> params, Long lineId) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    public static Map<String, String> createLineStationRequestParams(Long preStationId, Long stationId, String distance, String duration) {
        Map<String, String> params = new HashMap<>();

        params.put("preStationId", preStationId + "");
        params.put("stationId", stationId + "");
        params.put("distance", distance);
        params.put("duration", duration);

        return params;
    }

    public static void 지하철_노선에_역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return readLinesStation(lineId);
    }

    public static void 지하철_노선_상세정보_조회_시_역_정보_포함됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(1);
    }

    public static void 지하철_노선에_여러개의_역이_순서대로_등록됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
    }

    public static void 지하철_노선에_여러개의_역이_순서없이_등록됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 3L, 2L));
    }

    public static void 지하철_노선에_이미_존재_역_등록_실패됨(ExtractableResponse<Response> lineStationResponse) {
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(lineStationResponse.body().asString()).contains("지하철 역이 해당 노선에 이미 등록되어 있습니다");
    }
    public static void 존재하지_않는_역_등록_실패(ExtractableResponse<Response> lineStationResponse) {
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(lineStationResponse.body().asString()).contains("존재하지 않는 역은 등록이 불가능 합니다.");
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

    public static ExtractableResponse<Response> readLinesStation(Long lineId) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all().
                when().
                delete("/lines/{lineId}/stations/{stationId}", lineId, stationId).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선에_지하철역_제외_확인됨(ExtractableResponse<Response> response, Long stationId) {
        LineResponse lineResponse = response.as(LineResponse.class);

        Boolean isRemoved = lineResponse.getStations().stream()
                .noneMatch(it -> it.getStation().getId() == stationId);

        assertThat(isRemoved).isTrue();
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<Long> orderedList) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(orderedList);
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(deleteResponse.body().asString()).contains("노선에 존재하지 않는 역은 삭제가 불가능 합니다");
    }
}
