package subway;

import config.fixtures.subway.StationLineMockData;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.StationLineRequest;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.fixtures.subway.StationLineMockData.createMockRequest1;
import static config.fixtures.subway.StationLineMockData.createMockRequest2;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * When  지하철 노선이 생성된다
     * Then  지하철 노선 목록 조회 시 생성된 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // given
        StationLineRequest mockRequest = createMockRequest1();

        // when
        createStationLineRequest(mockRequest);
        JsonPath allStationLine = findAllStationLines();

        // then
        assertAll(
            () -> assertThat(allStationLine.getList("name")).containsAnyOf(mockRequest.getName()),
            () -> assertThat(allStationLine.getList("color")).containsAnyOf(mockRequest.getColor()),
            () -> assertThat(allStationLine.getList("upStationId")).containsAnyOf(mockRequest.getUpStationId()),
            () -> assertThat(allStationLine.getList("downStationId")).containsAnyOf(mockRequest.getDownStationId()),
            () -> assertThat(allStationLine.getList("distance")).containsAnyOf(mockRequest.getDistance())
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When  지하철 노선 목록을 조회하면
     * Then  지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllStationLine() {
        // given
        StationLineRequest mockRequest1 = createMockRequest1();
        StationLineRequest mockRequest2 = createMockRequest2();

        createStationLineRequest(mockRequest1);
        createStationLineRequest(mockRequest2);

        // when
        JsonPath allStationLine = findAllStationLines();

        // then
        assertAll(
                () -> assertThat(allStationLine.getList("name"))
                        .containsAnyOf(mockRequest1.getName(), mockRequest2.getName()),
                () -> assertThat(allStationLine.getList("color"))
                        .containsAnyOf(mockRequest1.getColor(), mockRequest2.getColor()),
                () -> assertThat(allStationLine.getList("upStationId"))
                        .containsAnyOf(mockRequest1.getUpStationId(), mockRequest2.getUpStationId()),
                () -> assertThat(allStationLine.getList("downStationId"))
                        .containsAnyOf(mockRequest1.getDownStationId(), mockRequest2.getDownStationId()),
                () -> assertThat(allStationLine.getList("distance"))
                        .containsAnyOf(mockRequest1.getDistance(), mockRequest2.getDistance())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 조회하면
     * Then  생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStationLine() {
        // given
        StationLineRequest mockRequest1 = createMockRequest1();
        ExtractableResponse<Response> response = createStationLineRequest(mockRequest1);

        // when
        JsonPath stationLine = findStationLine(getCreatedLocationId(response));

        // then
        assertAll(
                () -> assertThat(stationLine.get("name").toString())
                        .isEqualTo(mockRequest1.getName()),
                () -> assertThat(stationLine.get("color").toString())
                        .isEqualTo(mockRequest1.getColor()),
                () -> assertThat(Integer.parseInt(stationLine.get("upStationId").toString()))
                        .isEqualTo(mockRequest1.getUpStationId()),
                () -> assertThat(Integer.parseInt(stationLine.get("downStationId").toString()))
                        .isEqualTo(mockRequest1.getDownStationId()),
                () -> assertThat(Integer.parseInt(stationLine.get("distance").toString()))
                        .isEqualTo(mockRequest1.getDistance())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 수정하면
     * Then  해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given
        StationLineRequest mockRequest1 = createMockRequest1();
        StationLineRequest mockRequest2 = createMockRequest2();

        ExtractableResponse<Response> createResponse = createStationLineRequest(mockRequest1);

        // when
        updateStationLineRequest(mockRequest2, getCreatedLocationId(createResponse));

        JsonPath updatedStationLine = findStationLine(getCreatedLocationId(createResponse));

        // then
        assertAll(
                () -> assertThat(updatedStationLine.get("name").toString())
                        .isEqualTo(mockRequest2.getName()),
                () -> assertThat(updatedStationLine.get("color").toString())
                        .isEqualTo(mockRequest2.getColor()),
                () -> assertThat(Integer.parseInt(updatedStationLine.get("upStationId").toString()))
                        .isEqualTo(mockRequest2.getUpStationId()),
                () -> assertThat(Integer.parseInt(updatedStationLine.get("downStationId").toString()))
                        .isEqualTo(mockRequest2.getDownStationId()),
                () -> assertThat(Integer.parseInt(updatedStationLine.get("distance").toString()))
                        .isEqualTo(mockRequest2.getDistance())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 삭제하면
     * Then  해당 지하철 노선 정보는 삭제된다.
     */
    @DirtiesContext(methodMode = BEFORE_METHOD)
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        StationLineRequest mockRequest1 = createMockRequest1();
        ExtractableResponse<Response> createResponse = createStationLineRequest(mockRequest1);

        // when
        deleteStationLineRequest(getCreatedLocationId(createResponse));

        JsonPath allStationLine = findAllStationLines();

        // then
        assertAll(
                () -> assertThat(allStationLine.getList("name", String.class))
                        .doesNotContain(mockRequest1.getName()),
                () -> assertThat(allStationLine.getList("color", String.class))
                        .doesNotContain(mockRequest1.getColor()),
                () -> assertThat(allStationLine.getList("upStationId", Integer.class))
                        .doesNotContain(mockRequest1.getUpStationId()),
                () -> assertThat(allStationLine.getList("downStationId", Integer.class))
                        .doesNotContain(mockRequest1.getDownStationId()),
                () -> assertThat(allStationLine.getList("distance", Integer.class))
                        .doesNotContain(mockRequest1.getDistance())
        );
    }

    /**
     * 지하철 노선 목록을 조회하고 jsonPath 반환
     *
     * @return 지하철 노선 목록을 나타내는 jsonPath 반환
     */
    private JsonPath findAllStationLines() {
        return given().log().all()
            .when()
                .get("/lines")
            .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    /**
     * 주어진 지하철 노선 ID에 해당하는 지하철 노선 정보 반환
     *
     * @param stationLineId 지하철 노선 ID
     * @return 지하철 노선 정보를 나타내는 jsonPath 반환
     */
    private JsonPath findStationLine(Long stationLineId) {
        return given().log().all()
                .when()
                    .get("/lines/" + stationLineId)
                .then().log().all()
                    .extract().jsonPath();
    }

    /**
     * 지하철 노선 생성 요청 후 Response 객체 반환
     *
     * @param request 지하철 요청 정보를 담은 객체
     * @return REST Assured 기반으로 생성된 Response 객체
     */
    private ExtractableResponse<Response> createStationLineRequest(StationLineRequest request) {
        return given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/lines")
        .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    /**
     * 지하철 노선 수정 요청 후 Response 객체 반환
     *
     * @param request 지하철 수정 정보를 담은 객체
     * @param stationLineId 수정할 지하철 노선 ID
     */
    private void updateStationLineRequest(StationLineRequest request, Long stationLineId) {
        given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .patch("/lines/" + stationLineId)
        .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().jsonPath();
    }

    /**
     * 지하철 노선 삭제 요청
     *
     * @param stationLineId 삭제할 지하철 노선 ID
     */
    private void deleteStationLineRequest(Long stationLineId) {
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .delete("/lines/" + stationLineId)
        .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    /**
     * 주어진 응답값으로부터 추출된 Location 속성에서 ID를 반환
     *
     * @param createResponse 응답값
     * @return 추출된 Location 속성의 ID
     */
    private Long getCreatedLocationId(ExtractableResponse<Response> createResponse) {
        return Long
                .parseLong(createResponse.header(HttpHeaders.LOCATION)
                        .substring(createResponse.header(HttpHeaders.LOCATION).lastIndexOf('/') + 1));
    }
}
