package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.SoftAssertions;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class StationAcceptanceAssertion {

    static void 지하철_역_생성됨(ExtractableResponse<Response> response) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
        softly.assertThat(response.header("Location"))
                .isNotBlank();
        softly.assertAll();
    }

    static void 지하철_역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static void 지하철_역_목록_응답됨(ExtractableResponse<Response> actualResponse, List<ExtractableResponse<Response>> expectedResponses) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualResponse.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = expectedResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = actualResponse.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        softly.assertThat(resultLineIds)
                .containsAll(expectedLineIds);
        softly.assertThat(actualResponse.statusCode())
                .isEqualTo(OK.value());
        softly.assertAll();
    }

    static void 지하철_역_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
