package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.AssertSteps.httpStatusCode_검증;
import static nextstep.subway.acceptance.LineAcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //given
        //when
        ExtractableResponse<Response> response = LineSteps.createLine(FIXTURE_RED);

        int statusCode = response.statusCode();

        //then
        httpStatusCode_검증(statusCode, CREATED.value());

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * when 동일한 이름의 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패 한다.
     */
    @DisplayName("지하철 노선 중복 이름 생성 실패")
    @Test
    void createLineDuplicationNameException() {
        //given
        LineSteps.createLine(FIXTURE_RED);

        //when
        ExtractableResponse<Response> response = LineSteps.createLine(FIXTURE_RED);

        int actual = response.statusCode();

        //then
        httpStatusCode_검증(actual, CONFLICT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        LineSteps.createLine(FIXTURE_RED);
        LineSteps.createLine(FIXTURE_BLUE);

        //when
        ExtractableResponse<Response> response = LineSteps.findLines();

        int statusCode = response.statusCode();
        List<String> namesOfLines = response.jsonPath().getList("name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(namesOfLines).containsExactly(RED_LINE_NAME, BLUE_LINE_NAME)
        );

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        ExtractableResponse<Response> redLine = LineSteps.createLine(FIXTURE_RED);
        String uri = redLine.header("Location");

        //when
        ExtractableResponse<Response> response = LineSteps.findLines(uri);

        int statusCode = response.statusCode();
        String findLineName = response.jsonPath().get("name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(findLineName).isEqualTo(RED_LINE_NAME)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        ExtractableResponse<Response> redLine = LineSteps.createLine(FIXTURE_RED);
        String uri = redLine.header("Location");

        //when
        ExtractableResponse<Response> response = LineSteps.updateLine(uri, FIXTURE_BLUE);

        int statusCode = response.statusCode();
        String updatedName = response.jsonPath().get("name");

        //then
        assertAll(
                () -> httpStatusCode_검증(statusCode, OK.value()),
                () -> assertThat(updatedName).isEqualTo(BLUE_LINE_NAME)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> redLine = LineSteps.createLine(FIXTURE_RED);
        String uri = redLine.header("Location");

        //when
        ExtractableResponse<Response> response = LineSteps.deleteLine(uri);

        int statusCode = response.statusCode();

        //then
        httpStatusCode_검증(statusCode, NO_CONTENT.value());
    }

}
