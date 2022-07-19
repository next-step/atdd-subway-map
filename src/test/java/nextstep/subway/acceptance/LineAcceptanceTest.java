package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("지하철 노선 관련 기능")
@Sql(scripts = {"classpath:sql/truncate.sql","classpath:sql/createStations.sql"})
class LineAcceptanceTest extends BaseAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선의 상행 종점역과 하행 종점역이 구간으로 등록된다.
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     * Then 지하철 노선의 구간목록의 첫 번째 구간의 상행역과 하행역이 노선의 상행 종점역과 하행 종점역이다.
     */
    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLineTest() {
        //when
        Map<String, Object> requestBody = setRequestBody("신분당선", "bg-red-600",1,2,10);
        ExtractableResponse<Response> response = createLine(requestBody);
        int lineId = response.jsonPath().getInt("id");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.jsonPath().getList("stations.id",Integer.class)).containsExactlyInAnyOrder(1,2);

        //then
        ExtractableResponse<Response> section = RestAssured
            .given().log().all()
            .when().get("/lines/{id}/sections", lineId)
            .then().log().all()
            .extract();

        assertThat(section.jsonPath().getInt("[0].upStationId")).isEqualTo(1);
        assertThat(section.jsonPath().getInt("[0].downStationId")).isEqualTo(2);
        assertThat(section.jsonPath().getInt("[0].distance")).isEqualTo(10);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철 노선목록을 조회한다.")
    void getLinesTest() {
        //given
        Map<String, Object> requestBody1 = setRequestBody("신분당선", "bg-red-600",1,2,10);
        Map<String, Object> requestBody2 = setRequestBody("2호선", "green",1,3,10);

        createLine(requestBody1);
        createLine(requestBody2);

        //when
        ExtractableResponse<Response> subwayLines = getLines();

        //then
        assertThat(subwayLines.jsonPath().getList("")).hasSize(2);
        assertThat(subwayLines.jsonPath().getList("id",Integer.class)).containsExactlyInAnyOrder(1,2);
        assertThat(subwayLines.jsonPath().getList("name",String.class)).containsExactlyInAnyOrder("신분당선","2호선");
        assertThat(subwayLines.jsonPath().getList("color",String.class)).containsExactlyInAnyOrder("bg-red-600","green");
        assertThat(subwayLines.jsonPath().getList("stations")).hasSize(2);

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLineTest() {
        //given
        Map<String, Object> requestBody1 = setRequestBody("신분당선", "bg-red-600",1,2,10);
        ExtractableResponse<Response> line = createLine(requestBody1);

        //when
        int createdLineId = line.jsonPath().getInt("id");
        ExtractableResponse<Response> subwayLines = getLine(createdLineId);

        //then
        assertThat(subwayLines.jsonPath().getInt("id")).isEqualTo(createdLineId);
        assertThat(subwayLines.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(subwayLines.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(subwayLines.jsonPath().getList("stations.id",Integer.class)).containsExactlyInAnyOrder(1,2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLineTest() {
        //given
        Map<String, Object> requestBody1 = setRequestBody("신분당선", "bg-red-600",1,2,10);
        ExtractableResponse<Response> line = createLine(requestBody1);

        //when
        Map<String, String> requestBody2 = setRequestBody("13호선", "bg-red-500");
        int createdLineId = line.jsonPath().getInt("id");
        ExtractableResponse<Response> updateResponse = updateLine(createdLineId, requestBody2);
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> subwayLine = getLine(createdLineId);
        assertThat(subwayLine.jsonPath().getInt("id")).isEqualTo(createdLineId);
        assertThat(subwayLine.jsonPath().getString("name")).isEqualTo("13호선");
        assertThat(subwayLine.jsonPath().getString("color")).isEqualTo("bg-red-500");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void deleteLineTest() {
        //given
        Map<String, Object> requestBody1 = setRequestBody("신분당선", "bg-red-600",1,2,10);
        ExtractableResponse<Response> line = createLine(requestBody1);

        //when
        int createdLineId = line.jsonPath().getInt("id");
        ExtractableResponse<Response> deleteResponse = deleteLine(createdLineId);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> subwayLines = getLines();
        assertThat(subwayLines.jsonPath().getList("name",String.class)).doesNotContain("신분당선");
    }

    private ExtractableResponse<Response> createLine(Map<String, Object> map) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(map)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    private Map<String, Object> setRequestBody(String name, String color, long upStationId, long downStationId,
        long distance) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("color", color);
        map.put("upStationId", upStationId);
        map.put("downStationId", downStationId);
        map.put("distance", distance);
        return map;
    }

    private Map<String, String> setRequestBody(String name, String color) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("color", color);
        return map;
    }

    private ExtractableResponse<Response> getLines() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getLine(long id) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{id}",id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> updateLine(long id, Map<String,String> requestBody) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().put("/lines/{id}",id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> deleteLine(long id) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{id}",id)
            .then().log().all()
            .extract();
    }
}
