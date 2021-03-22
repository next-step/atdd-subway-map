package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class SectionAcceptanceAssertion {
    static void 지하철_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(OK.value());
    }

    static void 지하철_구간_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static void 지하철_구간_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(OK.value());
    }

    static void 지하철_구간_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static void 지하철_구간_목록_응답됨(ExtractableResponse<Response> actualResponse, List<StationResponse> expectedResponses) {
        List<String> actualStations = toNames(actualResponse);
        List<String> expectedStations = toNames(expectedResponses);
        assertThat(actualStations)
                .isEqualTo(expectedStations);
    }

    private static List<String> toNames(List<StationResponse> stationResponses) {
        return stationResponses.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }

    private static List<String> toNames(ExtractableResponse<Response> response) {
        return toNames(response.as(LineResponse.class)
                .getStations());
    }


}
