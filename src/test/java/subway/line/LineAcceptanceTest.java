package subway.line;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"classpath:SQLScripts/00.clear-database.sql", "classpath:SQLScripts/01.station-data.sql"})
public class LineAcceptanceTest {
    static final String SBD_LINE_NAME = "신분당선";
    static final String BD_LINE_NAME = "분당선";
    static final String RED_LINE_COLOR = "bg-red-600";
    static final String GREEN_LINE_COLOR = "bg-green-600";
    static final Long STATION_ID_1 = 1L;
    static final Long STATION_ID_2 = 2L;
    static final Long STATION_ID_3 = 3L;
    static final Long DISTANCE_10 = 10L;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Response response = this.requestCreateLine(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = this.requestSearchLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(SBD_LINE_NAME);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void searchLines() {
        // given
        this.requestCreateLine(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10);
        this.requestCreateLine(BD_LINE_NAME, GREEN_LINE_COLOR, STATION_ID_1, STATION_ID_3, DISTANCE_10);

        // when
        List<String> lineNames = this.requestSearchLines().jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames).containsAnyOf(SBD_LINE_NAME, BD_LINE_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void searchLine() {
        // given
        String createdId = this.requestCreateLine(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10)
                .jsonPath().getString("id");

        // when
        String lineName = this.requestSearchLine(createdId).jsonPath().getString("name");

        // then
        assertThat(lineName).isEqualTo(SBD_LINE_NAME);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String createdId = this.requestCreateLine(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10)
                .jsonPath().getString("id");

        // when
        this.requestUpdateLine(createdId, BD_LINE_NAME, GREEN_LINE_COLOR);

        //then
        String updatedLineName = this.requestSearchLine(createdId).jsonPath().getString("name");
        assertThat(updatedLineName).isEqualTo(BD_LINE_NAME);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 지하철 노선이 삭제된다
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        String createdId = this.requestCreateLine(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10)
                .jsonPath().getString("id");
        // when
        assertThat(this.requestDeleteLine(createdId).statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> searchLines = this.requestSearchLines().jsonPath().getList("name", String.class);
        assertThat(searchLines).isEmpty();
    }

    private Response requestCreateLine(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE).when().post("/lines").then().log().all().extract().response();
    }

    private Response requestSearchLines() {
        return RestAssured.given().log().all().when().get("/lines").then().log().all().extract().response();
    }

    private Response requestSearchLine(String id) {
        return RestAssured.given().log().all().when().get("/lines/" + id).then().log().all().extract().response();
    }

    private Response requestUpdateLine(String id, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return RestAssured.given().body(params).contentType(MediaType.APPLICATION_JSON_VALUE).when().put("/lines/" + id).then().log().all().extract().response();
    }

    private Response requestDeleteLine(String id) {
        return RestAssured.given().log().all().when().delete("/lines/" + id).then().log().all().extract().response();
    }
}
