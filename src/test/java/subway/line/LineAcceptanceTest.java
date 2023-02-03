package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationAcceptanceTest.createStationByName;

@DisplayName("노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private final String 신분당선_이름 = "신분당선";
    private final String 신분당선_색 = "bg-red-600";
    private final String 분당선_이름 = "분당선";
    private final String 다른분당선_이름 = "다른분당선";
    private final String 다른분당선_색 = "bg-red-600";


    private final static LineRequest 신분당선 = LineRequest.of(
            "신분당선", "bg-red-600", 1L, 2L, 10L);
    private final static LineRequest 분당선 = LineRequest.of(
            "분당선", "bg-green-600", 1L, 3L, 15L);

    @BeforeEach
    void setUp() {
        createStationByName("지하철역");
        createStationByName("새로운지하철역");
        createStationByName("또다른지하철역");
    }

    /**
     * when 지하철 노선을 생성한다.
     * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void 노선_생성() {
        //when
        createLine(신분당선);
        List<String> lineNames = getLines()
                .jsonPath().getList("name", String.class);

        //Then
        assertThat(lineNames).containsAnyOf(신분당선_이름);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * when 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void 노선_목록_조회() {
        //Given
        createLine(신분당선);
        createLine(분당선);

        //When
        List<String> lineNames = getLines()
                .jsonPath().getList("name", String.class);

        //Then
        assertThat(lineNames).containsAll(List.of(신분당선_이름, 분당선_이름)).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void 노선_조회() {
        //Given
        Long id = getCreatedLineId(신분당선);

        //When
        ExtractableResponse<Response> response = readLine(id);

        //Then
        String name = response.jsonPath().get("name");
        String color = response.jsonPath().get("color");
        assertThat(name).isEqualTo(신분당선_이름);
        assertThat(color).isEqualTo(신분당선_색);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void 노선_수정() {
        //Given
        long updateId = getCreatedLineId(신분당선);

        //When
        updateLine(updateId, 다른분당선_이름, 다른분당선_색);
        ExtractableResponse<Response> response = readLine(updateId);

        //Then
        assertThat(response.jsonPath().getString("name")).isEqualTo(다른분당선_이름);
        assertThat(response.jsonPath().getString("color")).isEqualTo(다른분당선_색);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void 노선_삭제() {
        //Given
        long deleteId = getCreatedLineId(신분당선);
        createLine(분당선);

        //When
        deleteLine(deleteId);
        List<String> lineNames = getLines()
                .jsonPath().getList("name", String.class);

        //Then
        assertThat(lineNames).doesNotContain(신분당선_이름);
    }

    private static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete("lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> createLine(LineRequest request) {

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static long getCreatedLineId(LineRequest request) {
        return createLine(request)
                .jsonPath().getLong("id");
    }

    private static ExtractableResponse<Response> getLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> readLine(Long id) {
        return RestAssured.given().log().all()
                .when().get("lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> updateLine(Long id, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
}
