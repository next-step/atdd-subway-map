package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import subway.domain.LineRepository;
import subway.domain.StationRepository;

@DisplayName("지하철 노선 관련 기능")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;
    private Long upStationId;
    private Long downStationId;

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineAcceptanceTest(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        stationRepository.truncateTableStation();
        lineRepository.deleteAllAndRestartId();
        this.upStationId = createStationResponseBy("강남역").jsonPath().getLong("id");
        this.downStationId = createStationResponseBy("양재역").jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> createStationResponseBy(final String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    @DisplayName("지하철 노선 생성한다.")
    @Test
    void createLine() {
        // when
        String name = "신분당선";
        String color = "bg-red-600";
        int distance = 10;

        createLine(name, color, upStationId, downStationId, distance);

        // then
        ExtractableResponse<Response> response = showLinesResponse();

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getLong("id[0]")).isEqualTo(1),
                () -> assertThat(jsonPath.getList("name")).contains(name),
                () -> assertThat(jsonPath.getList("color")).contains(color),
                () -> assertThat(jsonPath.getList("stations.id[0]")).contains(upStationId.intValue(),
                        downStationId.intValue()),
                () -> assertThat(jsonPath.getList("distance")).contains(distance)
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        createLine("신분당선", "bg-red-600", upStationId, downStationId, 10);
        createLine("2호선", "bg-green-600", upStationId, downStationId, 20);

        // when
        ExtractableResponse<Response> response = showLinesResponse();

        // then
        assertThat(response.jsonPath().getList("id")).hasSize(2).contains(1, 2);
    }

    private static ExtractableResponse<Response> showLinesResponse() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private static void createLine(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        Map body = Map.of(
                "name", name,
                "color", color,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance
        );

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }
}
