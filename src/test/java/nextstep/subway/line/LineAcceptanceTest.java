package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.LineTestStep.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse response = 지하철_노선_생성_요청("신분당선", "red");

        // then
        지하철_노선_생성_확인(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        List<LineResponse> lineResponseList = 지하철_노선_목록_등록되어_있음();

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_확인(lineResponseList, response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(addedLineResponse.getId());

        // then
        지하철_노선_확인(addedLineResponse, response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red");

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청( "2호선", "green", addedLineResponse.getId());

        // then
        지하철_노선_수정_확인(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(addedLineResponse.getId());

        // then
        지하철_노선_삭제_확인(response);
    }

    @DisplayName("기존에 존재하는 노선 이름 으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        //given
        지하철_노선_등록되어_있음("신분당선", "red");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "yellow");

        // then
        노선_생성_실패됨(response);
    }
}
