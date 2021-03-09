package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineRequestStep.*;
import static nextstep.subway.line.LineVerificationStep.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        final ExtractableResponse<Response> 신분당선 = 지하철_노선_등록요청("신분당선","red");

        지하철_노선_등록됨(신분당선);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        final Long blueLineId = 지하철_노선_등록되어_있음("경강선", "blue");
        final Long redLineId = 지하철_노선_등록되어_있음("신분당선", "red");

        final ExtractableResponse<Response> response = 지하철_노선_목록조회요청();

        지하철_노선_목록조회됨(blueLineId, redLineId, response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        final Long lineId = 지하철_노선_등록되어_있음("경강선", "blue");

        final ExtractableResponse<Response> response = 지하철_노선_조회요청(lineId);

        지하철_노선_조회됨(lineId, response);
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        final Long lineId = 지하철_노선_등록되어_있음("경강선", "blue");

        final ExtractableResponse<Response> response = 지하철_노선_수정요청("신분당선", "red", lineId);

        지하철_노선_수정됨(lineId, response);
    }


    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        final Long lineId = 지하철_노선_등록되어_있음("경강선", "blue");

        final ExtractableResponse<Response> response = 지하철_노선_삭제요청(lineId);

        지하철_노선_삭제됨(response);
    }


}
