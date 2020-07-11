package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationRemoveAcceptanceStep {

    public static ExtractableResponse<Response> 노선의_지하철역_제거_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/lines/{lineId}/stations/{stationId}", lineId, stationId).
                then().
                log().all().
                extract();
    }

    public static void 노선의_지하철역_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 노선의_지하철역_제거_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
