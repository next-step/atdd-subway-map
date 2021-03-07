package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "red");

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성할 경우 에러 발생")
    @Test
    void createLineAboutExisting() {
        // given
        지하철_노선_생성_요청("신분당선", "red");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "red");

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse line1 = 지하철_노선_생성_요청("1호선", "blue").as(LineResponse.class);
        LineResponse line2 = 지하철_노선_생성_요청("2호선", "green").as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(line1, line2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse line = 지하철_노선_생성_요청("3호선", "orange").as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(line.getId());

        // then
        지하철_노선_응답됨(response, line);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse line = 지하철_노선_생성_요청("4호선", "sky").as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(line.getId(), "4호선-수정", "sky-수정");

        // then
        지하철_노선_수정됨(response, "4호선-수정", "sky-수정");
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse line = 지하철_노선_생성_요청("8호선", "pink").as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(line.getId());

        // then
        지하철_노선_삭제됨(response);
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode())
                  .isEqualTo(HttpStatus.CREATED.value());
    }

    private void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode())
                  .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_목록_포함됨(
        ExtractableResponse<Response> response,
        List<LineResponse> expected
    ) {
        List<LineResponse> lines = response.body().jsonPath().getList(".", LineResponse.class);

        Assertions.assertThat(lines.get(0).getName())
            .isEqualTo(expected.get(0).getName());

        Assertions.assertThat(lines.get(0).getColor())
            .isEqualTo(expected.get(0).getColor());

        Assertions.assertThat(lines.get(1).getName())
            .isEqualTo(expected.get(1).getName());

        Assertions.assertThat(lines.get(1).getColor())
            .isEqualTo(expected.get(1).getColor());
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse expected) {
        LineResponse line = response.as(LineResponse.class);

        Assertions.assertThat(line.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(line.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(line.getColor()).isEqualTo(expected.getColor());
    }

    private void 지하철_노선_수정됨(
        ExtractableResponse<Response> response,
        String expectedName,
        String expectedColor
    ) {
        LineResponse line = response.as(LineResponse.class);

        Assertions.assertThat(line.getName()).isEqualTo(expectedName);
        Assertions.assertThat(line.getColor()).isEqualTo(expectedColor);
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode())
                  .isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
