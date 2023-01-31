package subway;

import static org.assertj.core.api.Assertions.assertThat;
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

@DirtiesContext
@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @DisplayName("지하철노선을 생성한다.")
    @Sql({"/sql/truncate-table.sql", "/sql/insert-station-data.sql"})
    @Test
    void createLineTest() {
        // when
        createLine("2호선", "#92932", 1L, 2L, 100);

        // then
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLineListTest() {
        // given

        // when

        // then
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLineTest() {
        // given

        // when

        // then
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLineTest() {
        // given

        // when

        // then
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLineTest() {
        // given

        // when

        // then
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

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        return response;
    }

    private ExtractableResponse<Response> createStation(Long lineId, String stationName) {

        Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1);
        params.put("name", stationName);

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .post("/stations")
                        .then()
                        .log()
                        .all()
                        .extract();

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        return response;
    }
}
