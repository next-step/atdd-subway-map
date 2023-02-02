package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.fixture.StationLineTestFixtures;
import subway.line.StationLine;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.StationAcceptanceTest.createStation;

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

    private StationLine fourLine;
    private StationLine shinbundangLine;

    @BeforeEach
    void init() {
        fourLine = createFourLine();
        shinbundangLine = createShinbundangLine();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLineAndGetListLine() {
        // given
        ExtractableResponse<Response> createResponse = createStationLine(
                fourLine.getName(),
                fourLine.getColor(),
                fourLine.getUpStationId(),
                fourLine.getDownStationId(),
                fourLine.getDistance()
        );

        // then
        JsonPath getJsonPath = getStationLines().body().jsonPath();

        assertAll(
            () -> assertThat(getJsonPath.getList("id").size()).isEqualTo(1),
            () -> assertThat(getJsonPath.getList("name").get(0)).isEqualTo(fourLine.getName()),
            () -> assertThat(getJsonPath.getList("color").get(0)).isEqualTo(fourLine.getColor())
        );
    }

    /**
     * Given 지하철 노선을 생성고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("생성한 지하철 노선 1개를 조회할 수 있다.")
    @Test
    void createLineAndGetLine() {
        ExtractableResponse<Response> createResponse = createStationLine(
                shinbundangLine.getName(),
                shinbundangLine.getColor(),
                shinbundangLine.getUpStationId(),
                shinbundangLine.getDownStationId(),
                shinbundangLine.getDistance()
        );

        Long stationLineId = createResponse.body().jsonPath().getLong("id");

        // then
        ExtractableResponse<Response> getResponse = getStationLine(stationLineId);

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getResponse.body().jsonPath().getLong("id")).isEqualTo(1);
        assertThat(getResponse.body().jsonPath().getString("name")).isEqualTo(shinbundangLine.getName());
        assertThat(getResponse.body().jsonPath().getString("color")).isEqualTo(shinbundangLine.getColor());
    }

    /**
     * Given 2개의 지하철 역, 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("생성한 지하철 노선 2개를 조회할 수 있다.")
    @Test
    void twoCreateLineAndGetLines() {
        final String gangnamStationName = "강남역";
        final String pangyoStationName = "판교역";
        final String sadangStationName = "사당역";

        // given
        ExtractableResponse<Response> lineShinbundang = createStationLine(
                shinbundangLine.getName(),
                shinbundangLine.getColor(),
                shinbundangLine.getUpStationId(),
                shinbundangLine.getDownStationId(),
                shinbundangLine.getDistance()
        );

        ExtractableResponse<Response> gangnamStation = createStation(gangnamStationName);
        ExtractableResponse<Response> pangyoStation = createStation(pangyoStationName);

        ExtractableResponse<Response> line4 = createStationLine(
                fourLine.getName(),
                fourLine.getColor(),
                fourLine.getUpStationId(),
                fourLine.getDownStationId(),
                fourLine.getDistance()
        );

        ExtractableResponse<Response> sadangStation = createStation(sadangStationName);

        // when
        ExtractableResponse<Response> getResponses = getStationLines();

        // then
        assertThat(getResponses.body().jsonPath().getList("id").size()).isEqualTo(2);
        assertThat(getResponses.body().jsonPath().getList("name").size()).isEqualTo(2);
        assertThat(getResponses.body().jsonPath().getList("color").size()).isEqualTo(2);
        assertThat(getResponses.body().jsonPath().getList("stations").size()).isEqualTo(2);
        assertThat(getResponses.body().jsonPath().getList("stations.name[0]").get(0)).isEqualTo(gangnamStationName);
        assertThat(getResponses.body().jsonPath().getList("stations.name[0]").get(1)).isEqualTo(pangyoStationName);
        assertThat(getResponses.body().jsonPath().getList("stations.name[1]").get(0)).isEqualTo(sadangStationName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("생성한 지하철 노선을 수정한다.")
    @Test
    void putStationLine() {
        ExtractableResponse<Response> createResponse = createStationLine(
                shinbundangLine.getName(),
                shinbundangLine.getColor(),
                shinbundangLine.getUpStationId(),
                shinbundangLine.getDownStationId(),
                shinbundangLine.getDistance()
        );

        Long stationLineId = createResponse.body().jsonPath().getLong("id");

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
    @DisplayName("생성한 지하철 노선을 삭제한다.")
    @Test
    void deleteLineAndGet() {
        ExtractableResponse<Response> createResponse = createStationLine(
                shinbundangLine.getName(),
                shinbundangLine.getColor(),
                shinbundangLine.getUpStationId(),
                shinbundangLine.getDownStationId(),
                shinbundangLine.getDistance()
        );

        Long stationLineId = createResponse.body().jsonPath().getLong("id");

        ExtractableResponse<Response> deleteResponse = deleteStationLine(stationLineId);
        ExtractableResponse<Response> getStationLine = getStationLine(stationLineId);

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(getStationLine.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private StationLine createShinbundangLine() {
        return StationLineTestFixtures.fixture(
                "신분당선",
                "bg-red-600",
                1L,
                2L,
                10
        );
    }

    private StationLine createFourLine() {
        return StationLineTestFixtures.fixture(
                "4호선",
                "bg-blue-600",
                3L,
                4L,
                30
        );
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
            Integer distance
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
