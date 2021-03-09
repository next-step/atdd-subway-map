package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineRequestSteps.*;
import static nextstep.subway.line.LineVerificationSteps.*;
import static nextstep.subway.station.StationRequestSteps.지하철_역_등록_됨;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private LineRequest 신분당선;

    private StationResponse 역삼역;
    private LineRequest 이호선;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철_역_등록_됨("강남역").as(StationResponse.class);
        양재역 = 지하철_역_등록_됨("양재역").as(StationResponse.class);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 7);

        역삼역 = 지하철_역_등록_됨("역삼역").as(StationResponse.class);
        이호선 = new LineRequest("2호선", "bg-red-600", 강남역.getId(), 역삼역.getId(), 2);
    }

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // when
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성_됨(createLineResponse);
    }

    @Test
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    void createLineWithDuplicationName() {
        // given
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성_실패_됨(response);
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLines() {
        // given
        지하철_노선_생성_요청(신분당선);
        지하철_노선_생성_요청(이호선);

        // when
        ExtractableResponse<Response> readLinesResponse = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회_됨(readLinesResponse);
        지하철_노선_목록_조회_결과에_생성된_노선_포함_확인(readLinesResponse);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선);

        // when
        String uri = 생성된_지하철_노선_URI_경로_확인(createResponse);
        ExtractableResponse<Response> readLineResponse = 지하철_노선_조회_요청(uri);

        // then
        지하철_노선_조회_됨(readLineResponse);
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선);

        // when
        String uri = 생성된_지하철_노선_URI_경로_확인(createResponse);
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(uri, "구분당선", "bg-blue-600");

        // then
        지하철_노선_수정_됨(updateResponse);
    }

    @Test
    @DisplayName("존재하지 않는 노선을 수정한다.")
    void updateNonExistLine() {
        // when
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청("/lines/1", "구분당선", "bg-blue-600");

        // then
        지하철_노선_수정_실패_됨(updateResponse);
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선);

        // when
        String uri = 생성된_지하철_노선_URI_경로_확인(createResponse);
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(uri);

        // then
        지하철_노선_제거_됨(deleteResponse);
    }

    @Test
    @DisplayName("존재하지 않는 노선을 제거한다.")
    void deleteNonExistLine() {
        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청("/lines/1");

        // then
        지하철_노선_제거_실패_됨(deleteResponse);
    }
}
