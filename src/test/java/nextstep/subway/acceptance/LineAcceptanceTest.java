package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        String color = "bg-red-600";
        String name = "신분당선";

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_생성한다(color, name);

        // then
        LineTestStep.지하철_노선_생성_성공_검증하기(response);
    }

    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void createDuplicatedNameLine() {
        // given
        String color = "bg-red-600";
        String name = "신분당선";
        LineTestStep.지하철_노선을_생성한다(color, name);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_생성한다(color, name);

        // then
        LineTestStep.지하철_노선_중복이름_생성_실패_검증하기(response);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        String 이호선_색 = "bg-green-600";
        String 이호선_이름 = "2호선";
        LineTestStep.지하철_노선을_생성한다(이호선_색, 이호선_이름);
        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        LineTestStep.지하철_노선을_생성한다(신분당선_색, 신분당선_이름);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선_목록을_조회한다();


        // then
        LineTestStep.지하철_노선_목록_조회_시_두_노선이_있는지_검증하기(response,
                Arrays.asList(이호선_색, 신분당선_색),
                Arrays.asList(이호선_이름, 신분당선_이름));
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        Long 신분당선_생성_아이디 = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(신분당선_색, 신분당선_이름);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_조회한다(신분당선_생성_아이디);

        // then
        LineTestStep.지하철_노선_조회_성공_검증하기(response, 신분당선_생성_아이디, 신분당선_색, 신분당선_이름);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        Long 신분당선_생성_아이디 = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(신분당선_색, 신분당선_이름);
        String 수정_색 = "bg-red-400";
        String 수정_이름 = "신분당선_연장";

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_수정한다(신분당선_생성_아이디, 수정_색, 수정_이름);

        // then
        LineTestStep.지하철_노선_수정_성공_검증하기(response, 신분당선_생성_아이디, 수정_색, 수정_이름);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        Long 신분당선_생성_아이디 = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(신분당선_색, 신분당선_이름);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_삭제한다(신분당선_생성_아이디);

        // then
        LineTestStep.지하철_노선_삭제_성공_검증하기(response);
    }
}
