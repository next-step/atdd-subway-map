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
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineRequestTestDto;
import subway.station.domain.line.Line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    private static final int LENGTH_TWO = 2;
    private static final String SHINBUNDANG_LINE = "신분당선";
    private static final String RED = "bg-red-600";
    private static final long DISTANCE_TEN = 10L;
    private static final String BUNDANG_LINE = "분당선";
    private static final String GREEN = "bg-green-600";
    private static final long DISTANCE_FIFTEEN = 15L;
    private static final String ANOTHER_BUNDANG_LINE = "다른분당선";
    private static final String YELLOW = "bg-yellow-600";

    @LocalServerPort
    int port;

    StationAcceptanceTest stationAcceptanceTest;
    private Long firstStationId;
    private Long secondStationId;
    private Long thirdStationId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        stationAcceptanceTest = new StationAcceptanceTest();
        firstStationId = stationAcceptanceTest.saveStation("지하철역");
        secondStationId = stationAcceptanceTest.saveStation("새로운지하철역");
        thirdStationId = stationAcceptanceTest.saveStation("또다른지하철역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다 => 지하철 노선 목록의 이름 중에 1호선이 있다.
     */
    @Test
    void 지하철_노선_생성_후_목록_조회시_찾을_수_있다() {
        //when
        LineRequestTestDto shinBunDangLine = getRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        restAssuredSave(shinBunDangLine);

        //then
        ExtractableResponse<Response> findAllResponse = restAssuredFindAll();

        Assertions.assertThat(findAllResponse.jsonPath().getList("name")).contains(shinBunDangLine.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다. => 지하철 노선 목록 조회시 응답받은 List<Response>의 길이가 2이다.
     */
    @Test
    void 두_개의_지하철_노선_생성_후_목록_조회시_응답_받은_Response의_길이는_2이다() {
        //given
        LineRequestTestDto shinBunDangLine = getRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        restAssuredSave(shinBunDangLine);
        LineRequestTestDto bunDangLine = getRequestTestDto(BUNDANG_LINE, GREEN, firstStationId, thirdStationId, DISTANCE_FIFTEEN);
        restAssuredSave(bunDangLine);

        //when
        ExtractableResponse<Response> findAllResponse = restAssuredFindAll();

        //then
        List<Line> lines = findAllResponse.jsonPath().get("line");

        Assertions.assertThat(lines.size()).isEqualTo(LENGTH_TWO);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_생성_후_생성된_노선으로_조회시_노선의_정보를_알_수_있다() {
        //given
        LineRequestTestDto shinBunDangLine = new LineRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        Long id = restAssuredSave(shinBunDangLine);

        //when
        ExtractableResponse<Response> findByResponse = restAssuredFindBy(id);

        //then
        String lineName = findByResponse.jsonPath().get("name");
        String lineColor = findByResponse.jsonPath().get("color");

        Assertions.assertThat(lineName).isEqualTo(shinBunDangLine.getName());
        Assertions.assertThat(lineColor).isEqualTo(shinBunDangLine.getColor());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선을_생성하고_수정하면_노선_정보는_수정된다() {
        //given
        LineRequestTestDto shinBunDangLine = new LineRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        Long id = restAssuredSave(shinBunDangLine);

        //when
        ExtractableResponse<Response> updateResponse = restAssuredUpdate(id, ANOTHER_BUNDANG_LINE, YELLOW);

        //then
        String updatedName = updateResponse.body().jsonPath().get("name");
        String updatedColor = updateResponse.body().jsonPath().get("color");

        Assertions.assertThat(updatedName).isEqualTo(ANOTHER_BUNDANG_LINE);
        Assertions.assertThat(updatedColor).isEqualTo(YELLOW);

        restAssuredFindAll();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선을_생성하고_삭제하면_노선_정보는_삭제_된다() {
        //given
        LineRequestTestDto shinBunDangLine = new LineRequestTestDto(SHINBUNDANG_LINE, RED, firstStationId, secondStationId, DISTANCE_TEN);
        Long id = restAssuredSave(shinBunDangLine);

        //when
        restAssuredDelete(id);

        // then
        Assertions.assertThat(restAssuredFindAll().jsonPath().getList("name")).doesNotContain(shinBunDangLine.getName());
    }

    private LineRequestTestDto getRequestTestDto(String name, String color, Long upStationId, Long downStationId, Long distance) {
        return LineRequestTestDto.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }

    private ExtractableResponse<Response> restAssuredUpdate(Long id, String lineChangeName, String lineChangeColor) {
        Map<String, String> lineParams = new HashMap<>();
        lineParams.put("name", lineChangeName);
        lineParams.put("color", lineChangeColor);

        return restAssuredUpdate(lineParams, id);
    }

    private Long restAssuredSave(LineRequestTestDto testLineDto) {
        final Map<String, String> lineParams = new HashMap<>();
        putParams(lineParams, testLineDto);

        return restAssuredSaveLine(lineParams);
    }

    private ExtractableResponse<Response> restAssuredDelete(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    private ExtractableResponse<Response> restAssuredUpdate(Map<String, String> params, Long id) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> restAssuredFindBy(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> restAssuredFindAll() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    private void putParams(Map<String, String> params, LineRequestTestDto lineTestDto) {
        params.put("name", lineTestDto.getName());
        params.put("color", lineTestDto.getColor());
        params.put("upStationId", String.valueOf(lineTestDto.getUpStationId()));
        params.put("downStationId", String.valueOf(lineTestDto.getDownStationId()));
        params.put("distance", String.valueOf(lineTestDto.getDistance()));
    }

    private Long restAssuredSaveLine(Map<String, String> params) {
        return RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject("id", Long.class);
    }
}
