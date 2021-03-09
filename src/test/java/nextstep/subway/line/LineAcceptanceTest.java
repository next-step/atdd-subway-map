package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static nextstep.subway.utils.LineTestUtils.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final String LINE_DX_NAME = "신분당선";
    private static final String LINE_DX_COLOR = "bg-red-600";
    private static final String LINE_TWO_NAME = "2호선";
    private static final String LINE_TWO_COLOR = "bg-green-600";
    private static final String LINE_BUNDANG_NAME = "구분당선";
    private static final String LINE_BUNDANG_COLOR = "bg-blue-600";

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //given
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        // 지하철_노선_생성됨
        응답_상태코드_확인(response, HttpStatus.CREATED);
        응답_헤더_로케이션_값_있음(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // 지하철_노선_등록되어_있음
        Map<String, String> 이호선 = 노선_파라미터_설정(LINE_TWO_NAME, LINE_TWO_COLOR);
        ExtractableResponse<Response> 이호선응답 = 지하철_노선_생성_요청(이호선);

        // when
        ExtractableResponse<Response> 노선목록조회응답 = 지하철_노선_목록_조회_요청();

        // then
        응답_상태코드_확인(노선목록조회응답, HttpStatus.OK);
        지하철_노선_목록_포함됨(노선목록조회응답, 신분당선응답, 이호선응답);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> 노선조회응답 = 지하철_노선_조회_요청(신분당선응답);

        // then
        // 지하철_노선_응답됨
        응답_상태코드_확인(노선조회응답, HttpStatus.OK);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        // 지하철_노선_수정_요청
        Map<String, String> 구분당선 = 노선_파라미터_설정(LINE_BUNDANG_NAME, LINE_BUNDANG_COLOR);

        ExtractableResponse<Response> 노선수정응답 = 지하철_노선_수정_요청(신분당선응답, 구분당선);

        // then
        // 지하철_노선_수정됨
        응답_상태코드_확인(노선수정응답, HttpStatus.OK);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> 노선삭제응답 = 지하철_노선_제거_요청(신분당선응답);

        // then
        // 지하철_노선_삭제됨
        응답_상태코드_확인(노선삭제응답, HttpStatus.NO_CONTENT);
    }
}
