package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationRemoveAcceptanceStep {
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

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
