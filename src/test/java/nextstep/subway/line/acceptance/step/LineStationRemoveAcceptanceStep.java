package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationRemoveAcceptanceStep {

    public static final String DELIMITER = "/";

    public static ExtractableResponse<Response> 지하철_노선의_지하철역_제외_요청(ExtractableResponse<Response> lineResponse, ExtractableResponse<Response> stationResponse) {
        String lineLocation = lineResponse.header(HttpHeaders.LOCATION);
        String stationLocation = stationResponse.header(HttpHeaders.LOCATION);

        return RestAssured.given().log().all().
                delete(lineLocation + stationLocation)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선에_지하철역_제외_확인됨(ExtractableResponse<Response> lineDetailResponse, ExtractableResponse<Response> stationResponse, int expectedSize) {
        List<Long> stationIds = parseStationIds(lineDetailResponse);
        Long stationId = Long.parseLong(parseStationId(stationResponse));

        assertThat(stationIds).hasSize(expectedSize)
                .doesNotContain(stationId);
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> lineDetailResponse, List<ExtractableResponse<Response>> stationResponses) {
        List<Long> stationIds = parseStationIds(lineDetailResponse);
        Long[] expectedIdsInOrder = stationResponses.stream()
                .map(response -> parseStationId(response))
                .map(Long::parseLong)
                .toArray(Long[]::new);

        assertThat(stationIds).containsExactly(expectedIdsInOrder);
    }

    public static ExtractableResponse<Response> 지하철_노선에_등록되지_않은_역_제외_요청(ExtractableResponse<Response> lineResponse, ExtractableResponse<Response> stationResponse) {
        return 지하철_노선의_지하철역_제외_요청(lineResponse, stationResponse);
    }


    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static List<Long> parseStationIds(ExtractableResponse<Response> lineDetailResponse) {
        return lineDetailResponse.as(LineResponse.class).getStations().stream()
                .map(LineStationResponse::getStation)
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    private static String parseStationId(ExtractableResponse<Response> stationResponse) {
        return stationResponse.header(HttpHeaders.LOCATION).split(DELIMITER)[2];
    }
}
