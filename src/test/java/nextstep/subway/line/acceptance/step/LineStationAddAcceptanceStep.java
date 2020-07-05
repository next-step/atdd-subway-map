package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAddAcceptanceStep {

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(ExtractableResponse<Response> lineResponse, ExtractableResponse<Response> stationResponse) {
        Long lineId = lineResponse.as(LineResponse.class).getId();
        Long stationId = stationResponse.as(StationResponse.class).getId();

        Map<String, String> params = new HashMap<>();
        params.put("preStationId", "");
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
}
