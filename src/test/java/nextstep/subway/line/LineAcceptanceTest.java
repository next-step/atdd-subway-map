package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final String LINE_NAME_SHIN_BUN_DANG = "신분당선";
    private static final String LINE_COLOR_SHIN_BUN_DANG = "bg-red-600";

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // given & when - 지하철_노선_생성_요청
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(LINE_NAME_SHIN_BUN_DANG, LINE_COLOR_SHIN_BUN_DANG);

        // then - 지하철_노선_생성됨
        지하철_노선_생성_확인(createLineResponse, HttpStatus.CREATED);
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLines() {
        // given - 지하철_노선_등록되어_있음
        지하철_노선_생성_요청(LINE_NAME_SHIN_BUN_DANG, LINE_COLOR_SHIN_BUN_DANG);
        지하철_노선_생성_요청("2호선", "bg-green-600");

        // when - 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> readLinesResponse = 지하철_노선_목록_조회_요청();

        // then - 지하철_노선_목록_응답됨 & 지하철_노선_목록_포함됨
        지하철_노선_생성_응답코드_확인(readLinesResponse, HttpStatus.OK);
        지하철_노선_목록_포함_확인(readLinesResponse);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given - 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(LINE_NAME_SHIN_BUN_DANG, LINE_COLOR_SHIN_BUN_DANG);
        String uri = 지하철_노선_URI_경로_확인(createResponse);

        // when - 지하철_노선_조회_요청
        ExtractableResponse<Response> readLineResponse = 지하철_노선_조회_요청(uri);

        // then - 지하철_노선_응답됨
        지하철_노선_생성_응답코드_확인(readLineResponse, HttpStatus.OK);
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given - 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(LINE_NAME_SHIN_BUN_DANG, LINE_COLOR_SHIN_BUN_DANG);

        // when - 지하철_노선_수정_요청
        Long id = 수정할_지하철_노선_ID(createResponse);
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(id, "구분당선", "bg-blue-600");

        // then - 지하철_노선_수정됨
        지하철_노선_생성_응답코드_확인(updateResponse, HttpStatus.OK);
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given - 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(LINE_NAME_SHIN_BUN_DANG, LINE_COLOR_SHIN_BUN_DANG);

        String uri = 지하철_노선_URI_경로_확인(createResponse);

        // when - 지하철_노선_제거_요청
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(uri);

        // then - 지하철_노선_삭제됨
        지하철_노선_생성_응답코드_확인(deleteResponse, HttpStatus.NO_CONTENT);
    }

}
