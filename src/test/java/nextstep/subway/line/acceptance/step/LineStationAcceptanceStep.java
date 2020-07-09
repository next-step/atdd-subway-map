package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceStep {

    public static ExtractableResponse<Response> 지하철_노선에_지하철역을_등록한다(Long lineId, Long stationId, Long preStationId) {
        Map<String, String> params = getLineStationRequestParameterMap(preStationId, stationId);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선을_조회하는_요청이_성공(LineResponse lineResponse) {
        assertThat(lineResponse).isNotNull();
    }

    public static void 지하철_노선이_정렬되어있다(LineResponse lineResponse, ArrayList<Long> orderedStationList) {
        assertThat(lineResponse.getStations()).extracting(it -> it.getStation().getId())
                .containsExactlyElementsOf(orderedStationList);
    }

    public static void 지하철_노선의_역_갯수가_n개이다(LineResponse lineResponse, int n) {
        assertThat(lineResponse.getStations()).hasSize(n);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        return 지하철_노선을_생성한다(name, color, LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역을_제외한다(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete("/lines/{lineId}/stations/{stationId}", lineId, stationId)
                .then()
                .log()
                .all()
                .extract();
    }

    public static void 지하철_노선에_역이_포함되지_않는다(LineResponse lineResponse, Long stationId3) {
        assertThat(lineResponse.getStations())
                .extracting(LineStationResponse::getStation).extracting(StationResponse::getId)
                .doesNotContain(stationId3);
    }

    private static Map<String, String> getLineStationRequestParameterMap(Long preStationId, Long stationId) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId + "");
        params.put("stationId", stationId + "");
        params.put("distance", "4");
        params.put("duration", "2");
        return params;
    }
}
