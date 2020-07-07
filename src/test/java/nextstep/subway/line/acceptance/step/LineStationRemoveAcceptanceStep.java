package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationRemoveAcceptanceStep {
    public static ExtractableResponse<Response> 지하철_노선_지하철역_제외_요청(
            ExtractableResponse<Response> createLineResponse,
            ExtractableResponse<Response> addLineStationResponse
    ) {
        Long lineStationId = addLineStationResponse.as(LineStationResponse.class).getStation().getId();
        return 지하철_노선_지하철역_제외_요청(createLineResponse, lineStationId);
    }

    public static ExtractableResponse<Response> 지하철_노선_지하철역_제외_요청(ExtractableResponse<Response> createLineResponse,
                                                                  Long lineStationId) {
        Long lineId = createLineResponse.as(LineResponse.class).getId();
        return 지하철_노선_지하철역_제외_요청(lineId, lineStationId);
    }

    public static ExtractableResponse<Response> 지하철_노선_지하철역_제외_요청(Long lineId, Long lineStationId) {
        return RestAssured.given().log().all().
                when().
                delete("/lines/{lineId}/stations/{stationId}", lineId, lineStationId).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> removeLineStationResponse) {
        assertThat(removeLineStationResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> removeLineStationResponse) {
        assertThat(removeLineStationResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 지하철_노선에_지하철역_제외_확인됨(ExtractableResponse<Response> getLineResponse,
                                           ExtractableResponse<Response> addLineStationResponse) {
        List<Long> lineStationIds = getLineResponse.as(LineResponse.class).getStations().stream()
                .map(lineStation -> lineStation.getStation().getId())
                .collect(Collectors.toList());
        Long removedLineStationId = addLineStationResponse.as(LineStationResponse.class).getStation().getId();

        assertThat(lineStationIds).doesNotContain(removedLineStationId);
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> getLineResponse,
                                           List<ExtractableResponse<Response>> addLineStationResponses) {
        List<Long> actualLineStationIds = getLineResponse.as(LineResponse.class).getStations().stream()
                .map(lineStation -> lineStation.getStation().getId())
                .collect(Collectors.toList());

        List<Long> expectLineStationIds = addLineStationResponses.stream()
                .map(lineStation -> lineStation.as(LineStationResponse.class).getStation().getId())
                .collect(Collectors.toList());

        assertThat(actualLineStationIds).containsExactlyElementsOf(expectLineStationIds);
    }
}
