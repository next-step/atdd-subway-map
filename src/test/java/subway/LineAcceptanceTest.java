package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
        scripts = {
            "/sql/truncate-table.sql",
            "/sql/insert-station-data.sql",
            "/sql/insert-line-data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext
public class LineAcceptanceTest {

    @DisplayName("지하철노선을 생성한다.")
    @Sql({
        "/sql/truncate-table.sql",
        "/sql/insert-station-data.sql",
    })
    @Test
    void createLineTest() {
        // when
        var response = createLine("2호선", "#92932", 1L, 2L, 100);

        // then
        assertEquals(response.statusCode(), HttpStatus.CREATED.value());
        checkCanFindCreatedLineInLineList("2호선");
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLineListTest() {
        // given - SQL로 대체

        // when
        List<String> lineList = findAllLine();

        // then
        checkCanFindLineInLineList(lineList, "2호선", "3호선");
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLineTest() {
        // given - SQL로 대체

        // when
        String lineName = findLine(1L).jsonPath().getString("name");

        // then
        assertEquals(lineName, "2호선");
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLineTest() {
        // given - SQL로 대체

        // when
        var response = updateLineNameAndColor(1L, "#29834", "8호선");

        // then
        assertEquals(response.statusCode(), HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLineTest() {
        // given - SQL로 대체

        // when
        var response = deleteLine(1L);

        // then
        assertEquals(response.statusCode(), HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> createLine(
            String name, String color, Long downStationId, Long upStationId, Integer distance) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .post("/lines")
                        .then()
                        .log()
                        .all()
                        .extract();

        return response;
    }

    private void checkCanFindCreatedLineInLineList(String... createdLineName) {

        List<String> stationList =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get("/lines")
                        .then()
                        .log()
                        .all()
                        .extract()
                        .jsonPath()
                        .getList("name", String.class);

        assertThat(stationList).containsAll(List.of(createdLineName));
    }

    private List<String> findAllLine() {

        List<String> stationList =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get("/lines")
                        .then()
                        .log()
                        .all()
                        .extract()
                        .jsonPath()
                        .getList("name", String.class);

        return stationList;
    }

    private ExtractableResponse<Response> findLine(Long lineId) {
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .get("/lines/{id}", lineId)
                        .then()
                        .log()
                        .all()
                        .extract();

        return response;
    }

    private void checkCanFindLineInLineList(List<String> lineList, String... lineName) {

        assertAll(
                () -> assertThat(lineList).containsAll(List.of(lineName)),
                () -> assertEquals(lineList.size(), lineName.length));
    }

    private ExtractableResponse<Response> updateLineNameAndColor(
            Long lineId, String lineName, String lineColor) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .body(params)
                        .when()
                        .put("/lines/{id}", lineId)
                        .then()
                        .log()
                        .all()
                        .extract();

        return response;
    }

    private ExtractableResponse<Response> deleteLine(Long lineId) {

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .delete("/lines/{id}", lineId)
                        .then()
                        .log()
                        .all()
                        .extract();

        return response;
    }
}
