package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.domain.station.Station;
import subway.station.web.dto.SaveLineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    private static final int LENGTH_TWO = 2;
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다 => 지하철 노선 목록의 이름 중에 1호선이 있다.
     */
    @Test
    void 지하철_노선_생성_후_목록_조회시_찾을_수_있다() {
        //when
        saveStation("지하철역");
        saveStation("새로운지하철역");

        final Map<String, String> lineParams = new HashMap<>();
        LineTestDto shinBunDangLine = new LineTestDto("신분당선", "bg-red-600", 1L, 2L, 10L);
        saveLine(lineParams, shinBunDangLine);

        //then
        ExtractableResponse<Response> viewResponses = getViewResponses();

        Assertions.assertThat(viewResponses.body().jsonPath().getList("name")).contains(shinBunDangLine.getLineName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다. => 지하철 노선 목록 조회시 응답받은 List<Response>의 길이가 2이다.
     */
    @Test
    void 두_개의_지하철_노선_생성_후_목록_조회시_응답_받은_Response의_길이는_2이다() {
        //given
        saveStation("지하철역");
        saveStation("새로운지하철역");
        saveStation("또다른지하철역");

        final Map<String, String> lineParams = new HashMap<>();
        saveLine(lineParams, new LineTestDto("신분당선", "bg-red-600", 1L, 2L, 10L));
        saveLine(lineParams, new LineTestDto("분당선", "bg-green-600", 1L, 3L, 15L));

        //when
        ExtractableResponse<Response> viewResponse = getViewResponses();

        //then
        List<Station> stations = viewResponse.body().jsonPath().get();

        Assertions.assertThat(stations.size()).isEqualTo(LENGTH_TWO);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_생성_후_생성된_노선으로_조회시_노선의_정보를_알_수_있다() {
        //given
        saveStation("지하철역");
        saveStation("새로운지하철역");

        final Map<String, String> lineParams = new HashMap<>();
        LineTestDto shinBunDangLine = new LineTestDto("신분당선", "bg-red-600", 1L, 2L, 10L);
        Long id = saveLine(lineParams, shinBunDangLine).getId();

        //when
        ExtractableResponse<Response> findResponse = getFindResponse(id);

        //then
        String lineName = findResponse.body().jsonPath().get("name");
        String lineColor = findResponse.body().jsonPath().get("color");

        Assertions.assertThat(lineName).isEqualTo(shinBunDangLine.getLineName());
        Assertions.assertThat(lineColor).isEqualTo(shinBunDangLine.getLineColor());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선을_생성하고_수정하면_노선_정보는_수정된다() {
        //given
        saveStation("지하철역");
        saveStation("새로운지하철역");

        final Map<String, String> lineParams = new HashMap<>();
        LineTestDto shinBunDangLine = new LineTestDto("신분당선", "bg-red-600", 1L, 2L, 10L);
        Long id = saveLine(lineParams, shinBunDangLine).getId();

        //when
        String lineChangeName = "다른분당선";
        String lineChangeColor = "bg-red-600";
        ExtractableResponse<Response> updateResponse = getUpdateResponse(lineParams, id, lineChangeName, lineChangeColor);

        //then
        String updatedName = updateResponse.body().jsonPath().get("name");
        String updatedColor = updateResponse.body().jsonPath().get("color");

        Assertions.assertThat(updatedName).isEqualTo(lineChangeName);
        Assertions.assertThat(updatedColor).isEqualTo(lineChangeColor);

        getViewResponses();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선을_생성하고_삭제하면_노선_정보는_삭제_된다() {
        //given
        saveStation("지하철역");
        saveStation("새로운지하철역");

        final Map<String, String> lineParams = new HashMap<>();
        LineTestDto shinBunDangLine = new LineTestDto("신분당선", "bg-red-600", 1L, 2L, 10L);
        Long id = saveLine(lineParams, shinBunDangLine).getId();

        //when
        ExtractableResponse<Response> deleteResponse = getDeleteResponse(id);

        // then
        Assertions.assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> getUpdateResponse(Map<String, String> lineParams, Long id, String lineChangeName, String lineChangeColor) {
        lineParams.put("name", lineChangeName);
        lineParams.put("color", lineChangeColor);
        ExtractableResponse<Response> updateResponse = getUpdateResponse(lineParams, id);
        return updateResponse;
    }

    private SaveLineResponse saveLine(Map<String, String> lineParams, LineTestDto lineName) {
        putParams(lineParams, lineName);
        return getSaveLineResponse(lineParams);
    }

    private void saveStation(String stationName) {
        final Map<String, String> stationParams = new HashMap<>();
        stationParams.put("name", stationName);
        getStationResponse(stationParams);
    }

    private void getStationResponse(Map<String, String> stationParams) {
        ExtractableResponse<Response> createResponse = RestAssured
                .given()
                .body(stationParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .extract();

        Assertions.assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> getDeleteResponse(Long id) {
        return RestAssured
                .given()
                .when()
                .delete("/lines/" + id)
                .then()
                .extract();
    }

    private ExtractableResponse<Response> getUpdateResponse(Map<String, String> params, Long id) {
        ExtractableResponse<Response> updateResponse = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .extract();

        Assertions.assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        return updateResponse;
    }

    private ExtractableResponse<Response> getFindResponse(Long id) {
        return RestAssured
                .given()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getViewResponses() {
        ExtractableResponse<Response> viewResponse = RestAssured
                .given()
                .when().get("/lines")
                .then().log().all()
                .extract();

        Assertions.assertThat(viewResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return viewResponse;

    }

    private void putParams(Map<String, String> params, LineTestDto lineTestDto) {
        params.put("name", lineTestDto.getLineName());
        params.put("color", lineTestDto.getLineColor());
        params.put("upStationId", String.valueOf(lineTestDto.getUpStationId()));
        params.put("downStationId", String.valueOf(lineTestDto.getDownStationId()));
        params.put("distance", String.valueOf(lineTestDto.getDistance()));
    }

    private SaveLineResponse getSaveLineResponse(Map<String, String> params) {
        ExtractableResponse<Response> saveResponse = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Assertions.assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return saveResponse.jsonPath().getObject("", SaveLineResponse.class);
    }
}
