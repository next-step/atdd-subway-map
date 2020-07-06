package nextstep.subway.station.acceptance.step;

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

public class LineStationAcceptanceStep {

    public static ExtractableResponse<Response>
    지하철_노선에_지하철역_등록_요청(Long lineId, String preStationId, String stationId, String distance, String duration) {
        final Map<String, String> parameter = registerStationToLineParameter(preStationId, stationId, distance, duration);
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(parameter).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_상세정보_조회_요청(Long lineId) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철_노선_상세정보_응답됨(ExtractableResponse<Response> response, int countOfLine) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(countOfLine);
    }

    public static void 지하철_노선_등록_순서_검사(ExtractableResponse<Response> response, Long... orders) {
        List<Long> stationIds = response.as(LineResponse.class).getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(orders));
    }

    private static Map<String, String>
    registerStationToLineParameter(String preStationId, String stationId, String distance, String duration) {
        final Map<String, String> parameter = new HashMap<>();
        parameter.put("preStationId", preStationId);
        parameter.put("stationId", stationId);
        parameter.put("distance", distance);
        parameter.put("duration", duration);
        return parameter;
    }
}
