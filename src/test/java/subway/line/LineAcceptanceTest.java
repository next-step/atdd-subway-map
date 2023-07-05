package subway.line;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO: 지하철노선 생성
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // TODO: BEF USE SQL
        Integer stationId1 = this.getCreatedStationId("역1");
        Integer stationId2 = this.getCreatedStationId("역2");

        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", stationId1);
        params.put("downStationId", stationId2);
        params.put("distance", 10);

        // when
        Response response = this.requestCreateLine(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = this.requestSearchLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO: 지하철노선 목록 조회
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void searchLines() {
        // TODO: BEF USE SQL
        Integer stationId1 = this.getCreatedStationId("역1");
        Integer stationId2 = this.getCreatedStationId("역2");
        Integer stationId3 = this.getCreatedStationId("역3");
        // given
        Map<String, Object> params1 = new HashMap<>();
        params1.put("name", "신분당선");
        params1.put("color", "bg-red-600");
        params1.put("upStationId", stationId1);
        params1.put("downStationId", stationId2);
        params1.put("distance", 10);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("name", "분당선");
        params2.put("color", "bg-green-600");
        params2.put("upStationId", stationId1);
        params2.put("downStationId", stationId3);
        params2.put("distance", 20);

        this.requestCreateLine(params1);
        this.requestCreateLine(params2);

        // when
        List<String> lineNames = this.requestSearchLines().jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames).containsAnyOf("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO: 지하철노선 조회
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void searchLine() {
        // TODO: BEF USE SQL
        Integer stationId1 = this.getCreatedStationId("역1");
        Integer stationId2 = this.getCreatedStationId("역2");
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", stationId1);
        params.put("downStationId", stationId2);
        params.put("distance", 10);

        String createdId = this.requestCreateLine(params).jsonPath().getString("id");

        // when
        String lineName = this.requestSearchLine(createdId).jsonPath().getString("name");

        // then
        assertThat(lineName).isEqualTo("신분당선");
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO: 지하철노선 수정
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // TODO: BEF USE SQL
        Integer stationId1 = this.getCreatedStationId("역1");
        Integer stationId2 = this.getCreatedStationId("역2");
        // given
        Map<String, Object> createParam = new HashMap<>();
        createParam.put("name", "신분당선");
        createParam.put("color", "bg-red-600");
        createParam.put("upStationId", stationId1);
        createParam.put("downStationId", stationId2);
        createParam.put("distance", 10);

        String createdId = this.requestCreateLine(createParam).jsonPath().getString("id");

        // when
        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put("name", "다른분당선");
        updateParam.put("color", "bg-red-600");
        this.requestUpdateLine(createdId, updateParam);

        //then
        String updatedLineName = this.requestSearchLine(createdId).jsonPath().getString("name");
        assertThat(updatedLineName).isEqualTo("다른분당선");
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 지하철 노선이 삭제된다
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO: 지하철노선 삭제
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // TODO: BEF USE SQL
        Integer stationId1 = this.getCreatedStationId("역1");
        Integer stationId2 = this.getCreatedStationId("역2");
        // given
        Map<String, Object> createParam = new HashMap<>();
        createParam.put("name", "신분당선");
        createParam.put("color", "bg-red-600");
        createParam.put("upStationId", stationId1);
        createParam.put("downStationId", stationId2);
        createParam.put("distance", 10);

        String createdId = this.requestCreateLine(createParam).jsonPath().getString("id");

        // when
        assertThat(this.requestDeleteLine(createdId).statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> searchLines = this.requestSearchLines().jsonPath().getList("name", String.class);
        assertThat(searchLines).isEmpty();
    }


    private Response requestCreateLine(Map<String, Object> params) {
        return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract().response();
    }

    private Response requestSearchLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().response();
    }

    private Response requestSearchLine(String id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().response();
    }

    private Response requestUpdateLine(String id, Map<String, Object> params) {
        return RestAssured.given().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract().response();
    }

    private Response requestDeleteLine(String id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract().response();
    }

    //!!TODO @SQL 사용전 임시 코드
    private Integer getCreatedStationId(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return RestAssured.given().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().response().jsonPath().getInt("id");
    }
}
