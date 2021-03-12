package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StationTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.Map;

import static nextstep.subway.utils.LineTestUtils.*;
import static nextstep.subway.utils.StationTestUtils.역_파라미터_설정;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final String LINE_DX_NAME = "신분당선";
    private static final String LINE_DX_COLOR = "bg-red-600";
    private static final String LINE_TWO_NAME = "2호선";
    private static final String LINE_TWO_COLOR = "bg-green-600";

    private StationResponse 판교역응답;
    private StationResponse 정자역응답;
    private StationResponse 신촌역응답;
    private StationResponse 홍대입구역응답;

    @BeforeEach
    public void before() {
        Map<String, String> 판교역 = 역_파라미터_설정("판교역");
        Map<String, String> 정자역 = 역_파라미터_설정("정자역");

        Map<String, String> 신촌역 = 역_파라미터_설정("신촌역");
        Map<String, String> 홍대입구역 = 역_파라미터_설정("홍대입구역");

        판교역응답 = StationTestUtils.역_생성_요청(판교역).as(StationResponse.class);
        정자역응답 = StationTestUtils.역_생성_요청(정자역).as(StationResponse.class);
        신촌역응답 = StationTestUtils.역_생성_요청(신촌역).as(StationResponse.class);
        홍대입구역응답 = StationTestUtils.역_생성_요청(홍대입구역).as(StationResponse.class);
    }


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //given
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR, 판교역응답.getId(), 정자역응답.getId(), 10);

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
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR, 판교역응답.getId(), 정자역응답.getId(), 10);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // 지하철_노선_등록되어_있음
        Map<String, String> 이호선 = 노선_파라미터_설정(LINE_TWO_NAME, LINE_TWO_COLOR, 신촌역응답.getId(), 홍대입구역응답.getId(), 10);
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
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR, 판교역응답.getId(), 정자역응답.getId(), 10);
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
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR, 판교역응답.getId(), 정자역응답.getId(), 10);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        // 지하철_노선_수정_요청
        Map<String, String> 이호선 = 노선_파라미터_설정(LINE_TWO_NAME, LINE_TWO_COLOR, 신촌역응답.getId(), 홍대입구역응답.getId(), 10);

        ExtractableResponse<Response> 노선수정응답 = 지하철_노선_수정_요청(신분당선응답, 이호선);

        // then
        // 지하철_노선_수정됨
        응답_상태코드_확인(노선수정응답, HttpStatus.OK);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR, 판교역응답.getId(), 정자역응답.getId(), 10);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> 노선삭제응답 = 지하철_노선_제거_요청(신분당선응답);

        // then
        // 지하철_노선_삭제됨
        응답_상태코드_확인(노선삭제응답, HttpStatus.NO_CONTENT);
    }
}
