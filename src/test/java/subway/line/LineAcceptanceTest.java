package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import subway.station.StationAcceptanceTest;
import subway.station.StationRepository;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {
    private Long upStationId;
    private Long downStationId;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    void beforeEach() {
        stationRepository.deleteAll();
        lineRepository.deleteAll();
        upStationId = StationAcceptanceTest.createStation("upStation");
        downStationId = StationAcceptanceTest.createStation("downStation");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    @Order(1)
    void createLine() {
        // Given
        final String name = "신분당선";
        final String color = "bg-red-600";
        final Integer distance = 10;

        // When
        LineResponse lineResponse = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(
                    LineRequest.builder()
                        .name(name)
                        .color(color)
                        .upStationId(upStationId)
                        .downStationId(downStationId)
                        .distance(distance)
                        .build()
                )
            .when()
                .post("/lines")
            .then()
                .log().all()
                .extract().jsonPath().getObject("$", LineResponse.class);

        // Then
        List<LineResponse> lines = findAllLines();
        assertThat(lines)
            .contains(lineResponse);
    }



    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    @Order(2)
    void searchLines() {
        // Given
        Long upStationId2 = StationAcceptanceTest.createStation("upStation");
        Long downStationId2 = StationAcceptanceTest.createStation("downStation");
        LineResponse givenLine1 = createLine("1호선", "bg-blue-000", upStationId, downStationId, 20);
        LineResponse givenLine2 = createLine("2호선", "bg-green-000", upStationId2, downStationId2, 13);

        // When
        List<LineResponse> lineResponses = RestAssured
            .given()
                .contentType(ContentType.JSON)
            .when()
                .get("/lines")
            .then()
                .log().all()
                .extract().jsonPath().getList("$", LineResponse.class);

        // Then
        assertThat(lineResponses)
            .contains(givenLine1, givenLine2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    @Order(3)
    void searchLine() {
        // Given
        LineResponse givenLine = createLine("1호선", "bg-blue-000", upStationId, downStationId, 20);

        // When
        LineResponse lineResponse = RestAssured
            .given()
            .when()
                .get("/lines/{id}", givenLine.getId())
            .then()
                .log().all()
                .extract().jsonPath().getObject("$", LineResponse.class);

        // Then
        assertThat(lineResponse)
            .isEqualTo(givenLine);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    @Order(4)
    void modifyLine() {
        // Given
        LineResponse givenLine = createLine("1호선", "bg-blue-000", upStationId, downStationId, 20);
        final String givenModifiedName = "다른분당선";
        final String givenModifiedColor = "bg-red-600";

        // When
        ExtractableResponse<Response> response = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(
                    LineModifyRequest.builder()
                        .name(givenModifiedName)
                        .color(givenModifiedColor)
                        .build()
                )
            .when()
                .put("/lines/{id}", givenLine.getId())
            .then()
                .log().all()
                .extract();

        // Then
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());

        LineResponse modifiedLineResponse = findLine(givenLine.getId());
        assertThat(modifiedLineResponse)
            .hasFieldOrPropertyWithValue("name", givenModifiedName)
            .hasFieldOrPropertyWithValue("color", givenModifiedColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    @Order(5)
    void deleteLine() {
        // Given
        LineResponse givenLine = createLine("1호선", "bg-blue-000", upStationId, downStationId, 20);

        // When
        ExtractableResponse<Response> response = RestAssured
            .given()
            .when()
                .delete("/lines/{id}", givenLine.getId())
            .then()
                .log().all()
                .extract();

        // Then
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> findLineResponse = RestAssured
            .given()
            .when()
                .get("/lines/{id}", givenLine.getId())
            .then()
                .log().all()
                .extract();

        assertThat(findLineResponse.statusCode())
            .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private List<LineResponse> findAllLines() {
        return RestAssured
            .given()
            .when()
                .get("/lines")
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList("$", LineResponse.class);
    }

    private LineResponse findLine(Long lineId) {
        return RestAssured
            .given()
            .when()
                .get("/lines/{id}", lineId)
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getObject("$", LineResponse.class);
    }

    private LineResponse createLine(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        return RestAssured
            .given()
                .contentType(ContentType.JSON)
            .body(
                LineRequest.builder()
                    .name(name)
                    .color(color)
                    .upStationId(upStationId)
                    .downStationId(downStationId)
                    .distance(distance)
                    .build()
            )
            .when()
                .post("/lines")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject("$", LineResponse.class);
    }
}
