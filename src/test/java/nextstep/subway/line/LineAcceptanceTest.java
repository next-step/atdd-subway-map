package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 삼성역;
    private StationResponse 신길역;
    private StationResponse 대방역;
    private LineRequest 이호선정보;
    private LineRequest 일호선정보;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        삼성역 = 지하철역_생성_요청("삼성역").as(StationResponse.class);
        이호선정보 = new LineRequest("2호선", "green", 강남역.getId(), 삼성역.getId(), 10);

        신길역 = 지하철역_생성_요청("신길역").as(StationResponse.class);
        대방역 = 지하철역_생성_요청("대방역").as(StationResponse.class);
        일호선정보 = new LineRequest("1호선", "blue", 신길역.getId(), 대방역.getId(), 10);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(이호선정보);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 생성시 생성 실패")
    @Test
    void validateReduplicationLine() {
        // given
        지하철_노선_생성_요청(이호선정보);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(이호선정보);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_생성_요청(이호선정보);
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_생성_요청(일호선정보);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(createdResponse1, createdResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성_요청(이호선정보);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdResponse);

        // then
        지하철_노선_조회됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성_요청(이호선정보);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(일호선정보, createdResponse);

        // then
        지하철_노선_수정됨(response, 일호선정보);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성_요청(이호선정보);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createdResponse);

        // then
        지하철_노선_삭제됨(response);
    }
}
