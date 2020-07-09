package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAddAcceptanceStep {

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_상세정보_응답됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(1);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId == null ? null : String.valueOf(preStationId));
        params.put("stationId", String.valueOf(stationId));
        params.put("distance", String.valueOf(distance));
        params.put("duration",String.valueOf(duration));

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();
    }

    public static void 등록된_지하철역이_정렬되어_위치됨 (ExtractableResponse<Response> response, List<Long> expectedStationIds) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(expectedStationIds.size());
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제거_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all().
                when().
                delete("/lines/{lineId}/stations/{stationId}", lineId, stationId).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_제외_확인됨(ExtractableResponse<Response> response, Long stationId) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        List<Long> stationIds = response.as(LineResponse.class).getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());

        assertThat(stationIds).doesNotContain(stationId);
    }

}
