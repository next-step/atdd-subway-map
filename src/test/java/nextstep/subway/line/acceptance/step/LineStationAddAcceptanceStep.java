package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAddAcceptanceStep {
    public static void 등록된_지하철_역이_주어진_위치에_위치됨(ExtractableResponse<Response> response, ArrayList<Long> expectedOrderElements) {
        LineResponse lineResponse = response.as(LineResponse.class);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(expectedOrderElements);
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    public static void 지하철_노선_상세정보_응답됨(ExtractableResponse<Response> response, int expectedSize) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(expectedSize);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(ExtractableResponse<Response> createdLineResponse, ExtractableResponse<Response> createdStationResponse, String preStationId) {
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId = createdStationResponse.as(StationResponse.class).getId();
        Map<String, String> params2 = new HashMap<>();
        params2.put("preStationId", preStationId);
        params2.put("stationId", String.valueOf(stationId));
        params2.put("distance", "4");
        params2.put("duration", "2");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params2).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }
}
