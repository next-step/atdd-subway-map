package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public class StationVerificationStep {

    public static void 지하철역_생성됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, BAD_REQUEST);
    }

    public static void 지하철역_조회됨(ExtractableResponse<Response> response, Long ...stationIds) {
        응답코드_확인(response, OK);
        assertThat(getResultLines(response))
                .containsAll(Arrays.asList(stationIds));
    }

    public static void 지하철역_제거됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, NO_CONTENT);
    }

    private static void 응답코드_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private static List<Long> getResultLines(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList(".", StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
