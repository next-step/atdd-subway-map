package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import subway.line.LineModifyRequest;
import subway.line.LineRequest;
import subway.line.LineResponse;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // Given
        final String name = "신분당선";
        final String color = "bg-red-600";
        final Long upStationId = 1L;
        final Long downStationId = 2L;
        final Long distance = 10L;

        // When
        LineResponse lineResponse = RestAssured
            .given()
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
            .extract().jsonPath().get();

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
        LineResponse givenLine1 = createLine("1호선", "bg-blue-000", 3L, 4L, 20L);
        LineResponse givenLine2 = createLine("2호선", "bg-green-000", 5L, 6L, 13L);

        // When
        List<LineResponse> lineResponses = RestAssured
            .given()
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
    void searchLine() {
        // Given
        LineResponse givenLine = createLine("1호선", "bg-blue-000", 3L, 4L, 20L);

        // When
        LineResponse lineResponse = RestAssured
            .given()
            .when()
                .get("/lines/{id}", givenLine.getId())
            .then()
                .log().all()
                .extract().jsonPath().get();

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
        LineResponse givenLine = createLine("1호선", "bg-blue-000", 3L, 4L, 20L);
        final String givenModifiedName = "다른분당선";
        final String givenModifiedColor = "bg-red-600";

        // When
        ExtractableResponse<Response> response = RestAssured
            .given()
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
    void deleteLine() {
        // Given
        LineResponse givenLine = createLine("1호선", "bg-blue-000", 3L, 4L, 20L);

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
                .log().all()
                .extract().jsonPath().getList("$", LineResponse.class);
    }

    private LineResponse findLine(Long lineId) {
        return RestAssured
            .given()
            .when()
                .get("/lines/{id}", lineId)
            .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().jsonPath().get();
    }

    private LineResponse createLine(String name, String color, Long upStationId, Long downStationId, Long distance) {
        return RestAssured
            .given()
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
                .log().all()
                .extract().jsonPath().get();
    }
}
