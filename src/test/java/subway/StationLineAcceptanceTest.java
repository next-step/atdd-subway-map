package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * StationLineAcceptanceTest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLineAndGetListLine() {
        final String lineName = "4호선";
        final String color = "bg-blue-600";
        final Long upStationId = 1L;
        final Long downStationId = 20L;
        final String distance = "30";

        // given
        ExtractableResponse<Response> createResponse = createStationLine(lineName, color, upStationId, downStationId, distance);

        // then
        JsonPath getJsonPath = getStationLines().body().jsonPath();

        assertAll(
            () -> assertThat(getJsonPath.getList("id").size()).isEqualTo(1),
            () -> assertThat(getJsonPath.getList("name").get(0)).isEqualTo(lineName),
            () -> assertThat(getJsonPath.getList("color").get(0)).isEqualTo(color)
        );
    }

    /**
     * Given 지하철 노선을 생성고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 생성하고 지하철 노선 단건 조회시 생성한 노선을 찾을 수 있다.")
    @Test
    void createLineAndGetLine() {
        final String lineName = "신분당선";
        final String color = "bg-red-600";
        final Long upStationId = 1L;
        final Long downStationId = 10L;
        final String distance = "10";

        ExtractableResponse<Response> createResponse = createStationLine(lineName, color, upStationId, downStationId, distance);

        Long stationLineId = createResponse.body().jsonPath().getLong("id");

        // then
        ExtractableResponse<Response> getResponse = getStationLine(stationLineId);

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getResponse.body().jsonPath().getLong("id")).isEqualTo(1);
        assertThat(getResponse.body().jsonPath().getString("name")).isEqualTo(lineName);
        assertThat(getResponse.body().jsonPath().getString("color")).isEqualTo(color);
    }

    @DisplayName("상행 종점역이 하행 종점역보다 더 작은 아이디 값을 가져야 한다.")
    @Test
    void upStationLineIdLessThanDownStationId() {
        final String lineName = "신분당선";
        final String color = "bg-red-600";
        final Long upStationId = 11L;
        final Long downStationId = 10L;
        final String distance = "10";

        ExtractableResponse<Response> createResponse = createStationLine(lineName, color, upStationId, downStationId, distance);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("2개의 지하철 노선을 생성하고 지하철 목록 조회시 생성한 2개 노선을 찾을 수 있다.")
    @Test
    void twoCreateLineAndGetLines() {
        // given
        ExtractableResponse<Response> lineShinbundang = createStationLine("신분당선", "bg-red-600", 1L, 10L, "10");
        ExtractableResponse<Response> line4 = createStationLine("4호선", "bg-blue-600", 11L, 40L, "10");

        // when
        ExtractableResponse<Response> getResponses = getStationLines();

        // then
        assertThat(getResponses.body().jsonPath().getList("id").size()).isEqualTo(2);
        assertThat(getResponses.body().jsonPath().getList("name").size()).isEqualTo(2);
        assertThat(getResponses.body().jsonPath().getList("color").size()).isEqualTo(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 생성하고 수정한 다음 지하철 노선을 조회했을 때 정보가 수정되어 있다.")
    @Test
    void putStationLine() {
        ExtractableResponse<Response> lineShinbundang = createStationLine("신분당선", "bg-red-600", 1L, 10L, "10");
        Long stationLineId = lineShinbundang.body().jsonPath().getLong("id");

        final String updateLineName = "4호선";
        final String updateColor = "bg-blue-600";

        ExtractableResponse<Response> putResponse = putStationLine(stationLineId, updateLineName, updateColor);

        ExtractableResponse<Response> getStationLine = getStationLine(stationLineId);

        assertThat(putResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getStationLine.body().jsonPath().getString("name")).isEqualTo(updateLineName);
        assertThat(getStationLine.body().jsonPath().getString("color")).isEqualTo(updateColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 생성하고 지하철역 제거한 후 목록을 조회하면 생성한 역을 찾을 수 없다.")
    @Test
    void deleteLineAndGet() {
        ExtractableResponse<Response> lineShinbundang = createStationLine("신분당선", "bg-red-600", 1L, 10L, "10");
        Long stationLineId = lineShinbundang.body().jsonPath().getLong("id");

        ExtractableResponse<Response> deleteResponse = deleteStationLine(stationLineId);
        ExtractableResponse<Response> getStationLine = getStationLine(stationLineId);

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(getStationLine.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> deleteStationLine(Long stationLineId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}", stationLineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getStationLine(Long stationLineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}", stationLineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> putStationLine(
            Long stationLineId,
            String lineName,
            String color
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", stationLineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getStationLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createStationLine(
            String lineName,
            String color,
            Long upStationId,
            Long downStationId,
            String distance
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
