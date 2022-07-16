package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.StaticMethodUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
public class StationLineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // When
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        ExtractableResponse<Response> response = createStationLine(stationLineName, stationLineColor, upStationId, downStationId);
        Long createdStationLineId = extractIdInResponse(response);

        // Then
        List<Long> ids = extractIdsInListTypeResponse(getAllStationLines());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(ids).containsAnyOf(createdStationLineId);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선의 목록을 조회한다.")
    @Test
    void getStationLines() {
        // Given
        String stationLineName1 = "신분당선";
        String stationLineColor1 = "bg-red-600";
        String stationLineName2 = "신분당선2";
        String stationLineColor2 = "bg-red-6002";

        String upStationName1 = "지하철역";
        String downStationName1 = "새로운지하철역";
        String upStationName2 = "지하철역2";
        String downStationName2 = "새로운지하철역2";

        Long upStationId1 = extractIdInResponse(createStationWithName(upStationName1));
        Long downStationId1 = extractIdInResponse(createStationWithName(downStationName1));
        Long upStationId2 = extractIdInResponse(createStationWithName(upStationName2));
        Long downStationId2 = extractIdInResponse(createStationWithName(downStationName2));

        Long createdStationLineId1 = extractIdInResponse(createStationLine(stationLineName1, stationLineColor1, upStationId1, downStationId1));
        Long createdStationLineId2 = extractIdInResponse(createStationLine(stationLineName2, stationLineColor2, upStationId2, downStationId2));

        // When
        ExtractableResponse<Response> allStationLines = getAllStationLines();

        // Then
        List<Long> ids = extractIdsInListTypeResponse(allStationLines);
        assertThat(allStationLines.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ids).hasSize(2);
        assertThat(ids).contains(createdStationLineId1, createdStationLineId2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getStationLine() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        ExtractableResponse<Response> response = getStationLineWithId(createdStationLineId);

        // Then
        Long responseStationLineId = extractIdInResponse(response);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseStationLineId).isEqualTo(createdStationLineId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        String updateName = "다른분당선";
        String updateColor = "bg-red-601";

        Map<Object, Object> params = new HashMap<>();
        params.put("name", updateName);
        params.put("color", updateColor);

        ExtractableResponse response = updateStationLine(createdStationLineId, params);

        // Then
        JsonPath jsonPath = getStationLineWithId(createdStationLineId).jsonPath();
        String updatedName = jsonPath.getString("name");
        String updatedColor = jsonPath.getString("color");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedName).isEqualTo(updateName);
        assertThat(updatedColor).isEqualTo(updateColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteStationLines() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        ExtractableResponse<Response> deleteResponse = deleteStationLineWithId(createdStationLineId);

        // Then
        List<Long> ids = extractIdsInListTypeResponse(getAllStationLines());
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(ids).doesNotContain(createdStationLineId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선에 올바른 조건의 구간을 등록하고
     * When 해당 지하철 노선을 조회하면
     * Then 해당 지하철 노선에 추가된 구간이 조회된다.
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void success_createSection() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        String newStationName = "연결될지하철역";
        Long newStationId = extractIdInResponse(createStationWithName(newStationName));
        Long distance = 10L;

        ExtractableResponse<Response> response = createSection(createdStationLineId, downStationId, newStationId, distance);

        // When
        ExtractableResponse<Response> stationLineWithId = getStationLineWithId(createdStationLineId);

        // Then
        List<Long> stationIdIncludedSection = stationLineWithId.jsonPath()
                .getList("stations.id", Long.class);
        Long newDownStationId = stationIdIncludedSection.get(stationIdIncludedSection.size() - 1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(newDownStationId).isEqualTo(newStationId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 구간의 상행역이 해당 노선에 등록된 하행 종점역이 아닌 상태로 구간을 등록하면
     * Then 에러가 발생한다.
     */
    @DisplayName("지하철 구간을 등록하나 새로운 구간 상행역이 노선에 등록된 하행종점역이 아니여서 실패한다.")
    @Test
    void fail_createSection_새로운구간상행역이_노선에등록된_하행종점역이_아닌경우() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        String newStationName = "연결될지하철역";
        Long newStationId = extractIdInResponse(createStationWithName(newStationName));
        Long distance = 10L;

        ExtractableResponse<Response> response = createSection(createdStationLineId, upStationId, newStationId, distance);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 구간 하행역이 해당 노선에 이미 등록되어있는 구간을 등록하면
     * Then 에러가 발생한다.
     */
    @DisplayName("지하철 구간을 등록하나 새로운 구간 하행역이 해당 노선에 이미 등록되어있는 역이여서 실패한다.")
    @Test
    void fail_createSection_새로운구간하행역이_노선에_이미등록되어있는역인경우() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        Long distance = 10L;
        ExtractableResponse<Response> response = createSection(createdStationLineId, downStationId, upStationId, distance);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 생성하고, 생성한 지하철 노선에 올바른 조건의 구간을 등록하고
     * When 해당 지하철 노선에 등록된 하행 종점역을 제거하면
     * Then 해당 지하철 노선 조회시, 삭제한 역이 구간에 존재하지 않는다.
     */
    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void success_deleteSection() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        String newStationName = "연결될지하철역";
        Long newStationId = extractIdInResponse(createStationWithName(newStationName));
        Long distance = 10L;

        createSection(createdStationLineId, downStationId, newStationId, distance);

        // When
        ExtractableResponse<Response> response = deleteSection(createdStationLineId, newStationId);

        // Then
        List<Long> stationIdIncludedInSection = getStationLineWithId(createdStationLineId).jsonPath()
                .getList("stations.id", Long.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(stationIdIncludedInSection).doesNotContain(newStationId);

    }

    /**
     * Given 지하철 노선을 생성하고, 생성한 지하철 노선에 올바른 조건의 구간을 등록하고
     * When 해당 지하철 노선에 등록되었으나 하행종점역이 아닌 역을 삭제하면
     * Then 에러가 발생한다.
     */
    @DisplayName("지하철 구간을 제거하나 하행 종점역이 아니여서 실패한다.")
    @Test
    void fail_deleteSection_하행종점역이아닌경우() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        String newStationName = "연결될지하철역";
        Long newStationId = extractIdInResponse(createStationWithName(newStationName));
        Long distance = 10L;

        createSection(createdStationLineId, downStationId, newStationId, distance);

        // When
        ExtractableResponse<Response> response = deleteSection(createdStationLineId, upStationId);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 상행 종점역과 하행 종점역만 있는 지하철 노선을 생성하고
     * When 해당 지하철 노선을 삭제하면
     * Then 에러가 발생한다.
     */
    @DisplayName("지하철 구간을 제거하나 구간이 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우)여서 실패한다.")
    @Test
    void fail_deleteSection_상행종점역과_하행종점역만_있는경우() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        ExtractableResponse<Response> response = deleteSection(createdStationLineId, downStationId);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> createSection(Long lineId, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections", lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured.given()
                .log()
                .all()
                .param("stationId", stationId)
                .when()
                .delete("/lines/{lineId}/sections", lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> createStationLine(String stationLineName, String stationLineColor, Long upStationId, Long downStationId
    ) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", stationLineName);
        params.put("color", stationLineColor);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);

        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> getAllStationLines() {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> getStationLineWithId(Long id) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/lines/{id}", id)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse updateStationLine(Long lineId, Map<Object, Object> params) {
        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{lineId}", lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStationLineWithId(Long createdStationLineId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/lines/{id}", createdStationLineId)
                .then()
                .log()
                .all()
                .extract();
    }
}