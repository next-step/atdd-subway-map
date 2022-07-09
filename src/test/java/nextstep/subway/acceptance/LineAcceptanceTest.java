package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void canFindTheLineCreatedWhenLineWasCreated() {
        // when
        var lineName = "신분당선";
        var lineColor = "bg-red-600";
        var upStationId = 1L;
        var downStationId = 2L;
        var distance = 10L;
        var creationResponse = createLine(lineName, lineColor, upStationId, downStationId, distance);

        // then
        var lineNames = RestAssured
                .when()
                    .get("/lines")
                .then()
                    .extract().jsonPath().getList("name", String.class);

        assertAll(
                () -> assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(lineNames).containsExactlyInAnyOrder(lineName)
        );
    }

    private ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, Long distance) {
        var requestBody = Map.of(
                "name", name,
                "color", color,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance
        );

        return RestAssured
                .given()
                    .body(requestBody)
                .when().log().all()
                    .post("/lines")
                .then()
                    .extract();
    }
}
