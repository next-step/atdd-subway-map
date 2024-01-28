package subway;

import config.fixtures.subway.StationLineMockData;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationLineRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.fixtures.subway.StationLineMockData.createMockRequest1;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        JsonPath allStationLineNames = findAllStationLine();

        // then
        assertAll(
            () -> assertThat( allStationLineNames.getList("name")).containsAnyOf(mockRequest.getName()),
            () -> assertThat(allStationLineNames.getList("color")).containsAnyOf(mockRequest.getColor()),
            () -> assertThat(allStationLineNames.getList("upStationId")).containsAnyOf(mockRequest.getUpStationId()),
            () -> assertThat(allStationLineNames.getList("downStationId")).containsAnyOf(mockRequest.getDownStationId()),
            () -> assertThat(allStationLineNames.getList("distance")).containsAnyOf(mockRequest.getDistance())
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When  지하철 노선 목록을 조회하면
     * Then  지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllStationLines() {
        // given
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", StationLineMockData.stationLineName1);
        map1.put("color", StationLineMockData.stationLineColor1);
        map1.put("upStationId", String.valueOf(StationLineMockData.upStationId1));
        map1.put("downStationId", String.valueOf(StationLineMockData.downStationId1));
        map1.put("distance", String.valueOf(StationLineMockData.distance1));

        Map<String, String> map2 = new HashMap<>();
        map2.put("name", StationLineMockData.stationLineName2);
        map2.put("color", StationLineMockData.stationLineColor2);
        map2.put("upStationId", String.valueOf(StationLineMockData.upStationId2));
        map2.put("downStationId", String.valueOf(StationLineMockData.downStationId2));
        map2.put("distance", String.valueOf(StationLineMockData.distance2));

        given().log().all()
                .body(map1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        given().log().all()
                .body(map2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        // when
        List<String> stationLineNames = given()
                .when()
                .get("/lines")
                .then()
                .extract().jsonPath().getList("name");

        // then
        assertThat(stationLineNames)
                .containsAnyOf(StationLineMockData.stationLineName1, StationLineMockData.stationLineName2);
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
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", StationLineMockData.stationLineName1);
        map1.put("color", StationLineMockData.stationLineColor1);
        map1.put("upStationId", String.valueOf(StationLineMockData.upStationId1));
        map1.put("downStationId", String.valueOf(StationLineMockData.downStationId1));
        map1.put("distance", String.valueOf(StationLineMockData.distance1));

        ExtractableResponse<Response> response = given().log().all()
                .body(map1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        // when
        List<String> stationLineNames = given().log().all()
                .when()
                .get("/line" + getCreatedLocationId(response))
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(stationLineNames).containsAnyOf(StationLineMockData.stationLineName1);
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
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", StationLineMockData.stationLineName1);
        map1.put("color", StationLineMockData.stationLineColor1);
        map1.put("upStationId", String.valueOf(StationLineMockData.upStationId1));
        map1.put("downStationId", String.valueOf(StationLineMockData.downStationId1));
        map1.put("distance", String.valueOf(StationLineMockData.distance1));

        ExtractableResponse<Response> createResponse =
                given().log().all()
                        .body(map1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

        Map<String, String> map2 = new HashMap<>();
        map2.put("name", StationLineMockData.stationLineName2);
        map2.put("color", StationLineMockData.stationLineColor2);

        // when
        List<String> stationLineNames =
                given().log().all()
                        .body(map2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .patch("/lines/" + getCreatedLocationId(createResponse))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().jsonPath().getList("name", String.class);
        // then
        assertThat(stationLineNames).containsAnyOf(StationLineMockData.stationLineName2);

    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 삭제하면
     * Then  해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", StationLineMockData.stationLineName1);
        map1.put("color", StationLineMockData.stationLineColor1);
        map1.put("upStationId", String.valueOf(StationLineMockData.upStationId1));
        map1.put("downStationId", String.valueOf(StationLineMockData.downStationId1));
        map1.put("distance", String.valueOf(StationLineMockData.distance1));

        ExtractableResponse<Response> createResponse =
                given().log().all()
                        .body(map1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

        // when
        given().log().all()
                .body(map1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/" + getCreatedLocationId(createResponse))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract().jsonPath().getList("name", String.class);

        List<String> stationLineNames =
                given().log().all()
                        .when()
                        .get("/line" + getCreatedLocationId(createResponse))
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(stationLineNames).doesNotContain(StationLineMockData.stationLineName1);
    }

    /**
     * 지하철 노선 목록을 조회하고 jsonPath 반환
     *
     * @return 지하철 노선 목록을 나타내는 jsonPath 반환
     */
    private JsonPath findAllStationLine() {
        return given().log().all()
            .when()
                .get("/lines")
            .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    /**
     * 지하철 노선 생성 요청
     *
     * @param request 지하철 요청 정보를 담은 객체
     */
    private void createStationLineRequest(StationLineRequest request) {
        given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/lines")
        .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    /**
     * 주어진 응답값으로부터 추출된 Location 속성에서 ID를 반환
     *
     * @param createResponse 응답값
     * @return 추출된 Location 속성의 ID
     */
    private int getCreatedLocationId(ExtractableResponse<Response> createResponse) {
        return Integer
                .parseInt(createResponse.header(HttpHeaders.LOCATION)
                        .substring(createResponse.header(HttpHeaders.LOCATION).lastIndexOf('/') + 1));
    }
}
