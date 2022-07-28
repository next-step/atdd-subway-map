package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.UpdateLineRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    public static final String 다른분당선 = "다른분당선";
    public static final String 신분당선 = "신분당선";
    public static final String 분당선 = "분당선";

    public static final String BG_RED_600 = "bg-red-600";
    public static final String BG_BLUE_600 = "bg-blue-600";
    public static final String BG_GREEN_600 = "bg-green-600";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private StationAcceptanceTest stationAcceptanceTest;

    private Long 지하철역Id;
    private Long 새로운지하철역Id;
    private Long 또다른지하철역Id;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        stationAcceptanceTest = new StationAcceptanceTest();
        지하철역Id = stationAcceptanceTest.지하철역_생성("지하철역").jsonPath().getLong("id");
        새로운지하철역Id = stationAcceptanceTest.지하철역_생성("새로운지하철역").jsonPath().getLong("id");
        또다른지하철역Id = stationAcceptanceTest.지하철역_생성("또다른지하철역").jsonPath().getLong("id");
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createLine() {
        //when
        지하철_노선_생성_요청(신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10);

        //then
        ExtractableResponse<Response> linesResponse = 지하철_노선목록_조회_요청();
        지하철_노선목록에서_생성한_노선_검증(linesResponse, 신분당선, BG_RED_600);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회합니다.")
    @Test
    void getLines() {
        //given
        지하철_노선_생성_요청(신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10);
        지하철_노선_생성_요청(분당선, BG_GREEN_600, 지하철역Id, 또다른지하철역Id, 12);

        //when
        ExtractableResponse<Response> linesResponse = 지하철_노선목록_조회_요청();

        //then
        지하철_노선목록에서_생성한_노선개수_검증(linesResponse, 2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회합니다.")
    @Test
    void getLine() {
        //given
        Long 신분당선Id = 지하철_노선_생성_요청(신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10)
                .jsonPath()
                .getLong("id");

        //when
        ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청(신분당선Id);

        //then
        지하철_노선조회에서_생성한_노선_정보_검증(lineResponse, 신분당선, BG_RED_600, 2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정합니다.")
    @Test
    void updateLine() {
        //given
        Long 신분당선Id = 지하철_노선_생성_요청(신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10)
                .jsonPath()
                .getLong("id");

        UpdateLineRequest updateLineRequest = new UpdateLineRequest(다른분당선, BG_BLUE_600);

        //when
        지하철_노선_수정_요청(신분당선Id, updateLineRequest);

        //then
        ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(신분당선Id);
        지하철_노선조회에서_생성한_노선_정보_검증(getLineResponse, 다른분당선, BG_BLUE_600, 2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 제거합니다.")
    @Test
    void deleteLine() {
        //given
        Long 신분당선Id = 지하철_노선_생성_요청(LineAcceptanceTest.신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10)
                .jsonPath()
                .getLong("id");

        //when
        지하철_노선_제거_요청(신분당선Id);

        //then
//        EntityNotFoundException entityNotFoundException = Assertions.assertThrows(EntityNotFoundException.class, () -> 지하철_노선_조회_요청(신분당선Id), "해당하는 노선이 없습니다.");
//        assertThatThrownBy(() -> 지하철_노선_조회_요청(신분당선Id))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessage("해당하는 노선이 없습니다.");

    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then()
                .log().all()
                .extract();
        assertThatStatus(lineResponse, HttpStatus.CREATED);
        return lineResponse;
    }

    private ExtractableResponse<Response> 지하철_노선목록_조회_요청() {
        ExtractableResponse<Response> linesResponse = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then()
                .log().all()
                .extract();
        assertThatStatus(linesResponse, HttpStatus.OK);
        return linesResponse;
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(Long stationId) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", stationId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Long stationId, UpdateLineRequest updateLineRequest) {
        ExtractableResponse<Response> updateLineResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateLineRequest)
                .when().put("/lines/{id}", stationId)
                .then()
                .log().all()
                .extract();
        assertThatStatus(updateLineResponse, HttpStatus.OK);
        return updateLineResponse;
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(Long stationId) {
        ExtractableResponse<Response> deleteLineResponse = RestAssured.given().log().all()
                .when().delete("/lines/{id}", stationId)
                .then()
                .log().all()
                .extract();
        assertThatStatus(deleteLineResponse, HttpStatus.NO_CONTENT);
        return deleteLineResponse;
    }

    private void 지하철_노선조회에서_생성한_노선_정보_검증(ExtractableResponse<Response> lineResponse, String lineName, String lineColor, int stationCount) {
        assertThat(lineResponse.jsonPath().getString("name")).isEqualTo(lineName);
        assertThat(lineResponse.jsonPath().getString("color")).isEqualTo(lineColor);
        assertThat(lineResponse.jsonPath().getList("stations").size()).isEqualTo(stationCount);
    }

    private void 지하철_노선목록에서_생성한_노선개수_검증(ExtractableResponse<Response> linesResponse, int lineCount) {
        assertThat(linesResponse.jsonPath().getList("$")).hasSize(lineCount);
    }

    private void 지하철_노선목록에서_생성한_노선_검증(ExtractableResponse<Response> linesResponse, String lineName, String lineColor) {
        assertThat(linesResponse.jsonPath().getList("name")).containsExactly(lineName);
        assertThat(linesResponse.jsonPath().getList("color")).containsExactly(lineColor);
        assertThat(linesResponse.jsonPath().getList("stations")).hasSize(1);
    }

    private void assertThatStatus(ExtractableResponse<Response> response, HttpStatus expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus.value());
    }
}
