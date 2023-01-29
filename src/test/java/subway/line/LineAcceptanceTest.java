package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    public static final String LINE_1 = "1호선";
    public static final String LINE_2 = "2호선";
    public static final String BLUE_COLOR = "bg-blue-000";
    public static final String GREEN_COLOR = "bg-green-000";
    public static final String RED_COLOR = "bg-red-600";
    public static final String LINE_NEW_BOONDANG = "신분당선";
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
    void createLine() {
        // When
        LineResponse lineResponse = createLine(
            LINE_NEW_BOONDANG,
            RED_COLOR,
            upStationId,
            downStationId,
            10
        );

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
    void searchLines() {
        // Given
        Long upStationId2 = StationAcceptanceTest.createStation("upStation2");
        Long downStationId2 = StationAcceptanceTest.createStation("downStation2");
        LineResponse givenLine1 = createLine(LINE_1, BLUE_COLOR, upStationId, downStationId, 20);
        LineResponse givenLine2 = createLine(LINE_2, GREEN_COLOR, upStationId2, downStationId2, 13);

        // When
        List<LineResponse> lineResponses = findAllLines();

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
    void searchLine() {
        // Given
        LineResponse givenLine = createLine(LINE_1, BLUE_COLOR, upStationId, downStationId, 20);

        // When
        LineResponse lineResponse = findLine(givenLine.getId());

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
    void modifyLine() {
        // Given
        LineResponse givenLine = createLine(LINE_1, BLUE_COLOR, upStationId, downStationId, 20);
        final String givenModifiedName = "다른분당선";
        final String givenModifiedColor = RED_COLOR;

        // When
        ExtractableResponse<Response> response = modifyLine(
            givenLine.getId(), givenModifiedName, givenModifiedColor);

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
    void deleteLine() {
        // Given
        LineResponse givenLine = createLine(LINE_1, BLUE_COLOR, upStationId, downStationId, 20);

        // When
        ExtractableResponse<Response> response = deleteLine(givenLine.getId());

        // Then
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> findLineResponse = findLineById(givenLine.getId());

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
        return findLineById(lineId)
            .jsonPath().getObject("$", LineResponse.class);
    }

    private ExtractableResponse<Response> findLineById(Long lineId) {
        return RestAssured
            .given()
            .when()
                .get("/lines/{id}", lineId)
            .then()
                .extract();
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

    private static ExtractableResponse<Response> modifyLine(
        Long lineId, String modifiedName, String modifiedColor) {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                LineModifyRequest.builder()
                    .name(modifiedName)
                    .color(modifiedColor)
                    .build()
            )
            .when()
            .put("/lines/{id}", lineId)
            .then()
            .log().all()
            .extract();
    }

    private static ExtractableResponse<Response> deleteLine(
        Long lineId) {
        return RestAssured
            .given()
            .when()
            .delete("/lines/{id}", lineId)
            .then()
            .log().all()
            .extract();
    }
}
