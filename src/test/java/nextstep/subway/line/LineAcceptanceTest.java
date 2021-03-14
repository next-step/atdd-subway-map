package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역, 양재역, 역삼역;

    private LineRequest 신분당선, 노선2호선;

    public LineAcceptanceTest(){
        super(); // setUp메서드 상속을 받아서 사용
    }

    @BeforeEach
    public void setRequest(){
        강남역 = 지하철_역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철_역_생성_요청("양재역").as(StationResponse.class);
        역삼역 = 지하철_역_생성_요청("역삼역").as(StationResponse.class);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        노선2호선 = new LineRequest("2호선", "bg-green-600", 강남역.getId(), 양재역.getId(), 5);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        지하철_노선_응답됨(createResponse, response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> response1 = 지하철_노선_등록되어_있음(신분당선);
        ExtractableResponse<Response> response2 = 지하철_노선_등록되어_있음(노선2호선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(Arrays.asList(response1, response2), response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음(신분당선);
        LineRequest lineRequest = new LineRequest("구분당선", "bg-blue-600", 신분당선.getUpStationId(), 신분당선.getDownStationId(), 신분당선.getDistance());

        // when
        ExtractableResponse<Response> resultResponse = 지하철_노선_수정_요청(response, lineRequest);

        // then
        지하철_노선_수정됨(resultResponse);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 만든다")
    @Test
    void createLineWithDuplicateName(){
        // given
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성_실패됨(response);
    }
}
