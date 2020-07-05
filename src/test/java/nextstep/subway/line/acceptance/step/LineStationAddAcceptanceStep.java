package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAddAcceptanceStep {
    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(ExtractableResponse<Response> createLineResponse,
                                                                     ExtractableResponse<Response> createPreStationResponse,
                                                                     ExtractableResponse<Response> createStationResponse,
                                                                     Integer distance, Integer duration) {
        return 지하철_노선에_지하철역_등록_요청(createLineResponse, createPreStationResponse, createStationResponse, distance, duration);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(ExtractableResponse<Response> createLineResponse,
                                                                   ExtractableResponse<Response> createPreStationResponse,
                                                                   ExtractableResponse<Response> createStationResponse,
                                                                   Integer distance, Integer duration) {
        Long preStationId = Objects.nonNull(createPreStationResponse) ?
                createPreStationResponse.as(StationResponse.class).getId() : null;
        Long stationId = createStationResponse.as(StationResponse.class).getId();
        return 지하철_노선에_지하철역_등록_요청(createLineResponse, preStationId, stationId, distance, duration);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(ExtractableResponse<Response> createLineResponse,
                                                                   Long preStationId, Long stationId,
                                                                   Integer distance, Integer duration) {
        Long lineId = createLineResponse.as(LineResponse.class).getId();
        Map<String, String> params = 지하철_노선에_지하철역_등록_요청값(preStationId, stationId, distance, duration);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> addLineStationResponse) {
        assertThat(addLineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철_노선_상세정보_조회_시_역_정보_포함됨(ExtractableResponse<Response> lineReadResponse,
                                                 List<ExtractableResponse<Response>> stationCreateResponses) {
        LineResponse lineResponse = lineReadResponse.as(LineResponse.class);
        assertThat(lineResponse.getStations()).hasSize(stationCreateResponses.size());
    }

    public static void 지하철_노선에_지하철역_순서대로_등록됨(ExtractableResponse<Response> lineReadResponse,
                                             List<ExtractableResponse<Response>> stationCreateResponses) {
        List<Long> lineStationIds = lineReadResponse.as(LineResponse.class).getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());

        List<Long> stationIds = stationCreateResponses.stream()
                .map(response -> response.as(StationResponse.class).getId())
                .collect(Collectors.toList());

        assertThat(lineStationIds).containsExactlyElementsOf(stationIds);
    }

    public static void 지하철_노선에_지하철역_등록_중복_등록으로_실패됨(ExtractableResponse<Response> addLineStationResponse) {
        assertThat(addLineStationResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 지하철_노선에_지하철역_등록_존재하지않아_실패됨(ExtractableResponse<Response> addLineStationResponse) {
        assertThat(addLineStationResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private static Map<String, String> 지하철_노선에_지하철역_등록_요청값(Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", (Objects.nonNull(preStationId) ? String.valueOf(preStationId) : null));
        params.put("stationId", String.valueOf(stationId));
        params.put("distance", String.valueOf(distance));
        params.put("duration",String.valueOf(duration));
        return params;
    }
}
