package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
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

    private final LineRestAssured lineRestAssured;
    private final LineAssert lineAssert;

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineAcceptanceTest(
            final StationRepository stationRepository,
            final LineRepository lineRepository
    ) {
        this.lineRestAssured = new LineRestAssured();
        this.lineAssert = new LineAssert();
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

        lineRestAssured.createLine(name, color, upStationId, downStationId, distance);

        // then
        lineAssert.assertCreateLine(name, color, upStationId, downStationId, distance);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        LineRestAssured.createLine("신분당선", "bg-red-600", upStationId, downStationId, 10);
        LineRestAssured.createLine("2호선", "bg-green-600", upStationId, downStationId, 20);

        // when
        ExtractableResponse<Response> response = lineRestAssured.showLinesResponse();

        // then
        assertThat(response.jsonPath().getList("id")).hasSize(2).contains(1, 2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        String name = "신분당선";
        String color = "bg-red-600";
        int distance = 10;
        ExtractableResponse<Response> createLineResponse
                = lineRestAssured.createLine(name, color, upStationId, downStationId, distance);

        String location = createLineResponse.header("Location");

        // when, then
        lineAssert.assertShowLine(name, color, upStationId, downStationId, distance, location);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void editLine() {
        // given
        ExtractableResponse<Response> createLineResponse
                = lineRestAssured.createLine("신분당선", "bg-red-600", upStationId, downStationId, 10);

        String location = createLineResponse.header("Location");

        String name = "수정한 지하철 노선 이름";
        String color = "수정한 지하철 색상";
        int distance = 5;

        // when
        lineRestAssured.editLineResponse(location, name, color, distance);

        // then
        lineAssert.assertEditLine(location, name, upStationId, downStationId, color, distance);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createLineResponse
                = lineRestAssured.createLine("신분당선", "bg-red-600", upStationId, downStationId, 10);

        String location = createLineResponse.header("Location");

        // when
        lineRestAssured.deleteLineResponse(location);

        // then
        lineAssert.assertDeleteLine(location);
    }
}
