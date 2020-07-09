package nextstep.subway.line.acceptance.step;

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

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAddAcceptanceStep {

    public static ExtractableResponse<Response> 노선에_지하철역_등록되어_있음(Long lineId, Long stationId) {
        return 노선에_지하철역_등록_요청(lineId, null, stationId);
    }

    public static ExtractableResponse<Response> 노선에_지하철역_등록_요청(Long lineId, Long preStationId, Long stationId) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId == null ? "" : preStationId + "");
        params.put("stationId", stationId + "");
        params.put("distance", "4");
        params.put("duration", "2");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    public static void 노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 노선에_지하철역_포함됨(ExtractableResponse<Response> response, int expectedStationSize) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getStations()).hasSize(expectedStationSize);
    }

    public static void 노선의_지하철역들의_순서가_예상과_일치함(ExtractableResponse<Response> response, List<Long> expectedStationIds) {
        LineResponse lineResponse = response.as(LineResponse.class);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());

        assertThat(lineResponse.getStations()).hasSize(expectedStationIds.size());
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }
}
