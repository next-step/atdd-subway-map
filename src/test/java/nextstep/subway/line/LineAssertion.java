package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.api.SoftAssertions;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

public class LineAssertion {
    static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(CREATED.value());
    }

    static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(OK.value());
    }

    static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> actualResponse,
                              List<ExtractableResponse<Response>> responses) {
        List<Long> actualIds = actualResponse.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedIds = responses.stream()
                .map(response -> response.as(LineResponse.class))
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(actualIds).containsAll(expectedIds);
    }

    static void 지하철_노선_응답됨(ExtractableResponse<Response> actualResponse, ExtractableResponse<Response> expectedResponse) {
        LineResponse actualLine = actualResponse.as(LineResponse.class);
        LineResponse expectedLine = expectedResponse.as(LineResponse.class);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualLine.getId())
                .isEqualTo(expectedLine.getId());
        softly.assertThat(actualLine.getName())
                .isEqualTo(expectedLine.getName());
        softly.assertThat(actualLine.getColor())
                .isEqualTo(expectedLine.getColor());
        softly.assertAll();
    }

    static void 지하철_노선_수정됨(ExtractableResponse<Response> response, String expectedName, String expectedColor) {
        LineResponse line = response.as(LineResponse.class);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(line.getName())
                .isEqualTo(expectedName);
        softly.assertThat(line.getColor())
                .isEqualTo(expectedColor);
        softly.assertAll();
    }

    static void 지하철_노선_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(NO_CONTENT.value());
    }
}
