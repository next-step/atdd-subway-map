package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    // Station 인수테스트 재사용
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 역삼역;
    // TODO Map이 좋을까? 아니면 LineRequest가 좋을까?
    private Map<String, String> 신분당선;
    private Map<String, String> 노선2호선;

    @BeforeEach
    public void setUp(){
        // 랜덤포트 초기화
        RestAssured.port = port;
        databaseCleanup.execute();

        강남역 = 지하철_역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철_역_생성_요청("양재역").as(StationResponse.class);
        역삼역 = 지하철_역_생성_요청("역삼역").as(StationResponse.class);

        // 노선 생성에 필요한 파람값
        신분당선 = new HashMap<>();
        신분당선.put("name", "신분당선");
        신분당선.put("color", "bg-red-600");
        신분당선.put("upStationId", 강남역.getId() + "");
        신분당선.put("downStationId", 양재역.getId() + "");
        신분당선.put("distance", "10");

        노선2호선 = new HashMap<>();
        노선2호선.put("name", "2호선");
        노선2호선.put("color", "bg-green-600");
        노선2호선.put("upStationId", 강남역.getId() + "");
        노선2호선.put("downStationId", 역삼역.getId() + "");
        노선2호선.put("distance", "5");
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
        Map<String, String> params = new HashMap<>();
        params.put("name", "구분당선");
        params.put("color", "bg-blue-600");
        params.put("upStationId", 신분당선.get("upStationId"));
        params.put("downStationId", 신분당선.get("downStationId"));
        params.put("distance", 신분당선.get("distance"));

        // when
        ExtractableResponse<Response> resultResponse = 지하철_노선_수정_요청(response, params);

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
