package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import subway.domain.LineRepository;
import subway.domain.StationRepository;
import subway.station.StationRestAssured;

@DisplayName("지하철 노선 관련 기능")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private static final String LINE_NAME = "신분당선";
    private static final String COLOR = "bg-red-600";
    private static final int DISTANCE = 10;

    @LocalServerPort
    private int port;
    private Long upStationId;
    private Long downStationId;

    private final LineRestAssured lineRestAssured;
    private final LineAssert lineAssert;
    private final StationRestAssured stationRestAssured;

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineAcceptanceTest(
            final StationRepository stationRepository,
            final LineRepository lineRepository
    ) {
        this.lineRestAssured = new LineRestAssured();
        this.lineAssert = new LineAssert();
        this.stationRestAssured = new StationRestAssured();
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        stationRepository.truncateTableStation();
        lineRepository.deleteAllAndRestartId();
        this.upStationId = stationRestAssured.createStation("강남역").jsonPath().getLong("id");
        this.downStationId = stationRestAssured.createStation("양재역").jsonPath().getLong("id");
    }

    @DisplayName("지하철 노선 생성한다.")
    @Test
    void createLine() {
        // when
        lineRestAssured.createLine(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        // then
        lineAssert.assertCreateLine(LINE_NAME, COLOR, DISTANCE);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        lineRestAssured.createLine(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);
        long upStationId2 = stationRestAssured.createStation("신림역").jsonPath().getLong("id");
        long downStationId2 = stationRestAssured.createStation("노량진역").jsonPath().getLong("id");
        lineRestAssured.createLine("2호선", "bg-green-600", upStationId2, downStationId2, 20);

        // when
        ExtractableResponse<Response> response = lineRestAssured.showLines();

        // then
        assertThat(response.jsonPath().getList("id")).hasSize(2).contains(1, 2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        ExtractableResponse<Response> createLineResponse
                = lineRestAssured.createLine(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        String location = getLocation(createLineResponse);

        // when, then
        lineAssert.assertShowLine(LINE_NAME, COLOR, DISTANCE, location);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void editLine() {
        // given
        ExtractableResponse<Response> createLineResponse
                = lineRestAssured.createLine(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        String location = getLocation(createLineResponse);

        String name = "수정한 지하철 노선 이름";
        String color = "수정한 지하철 색상";
        int distance = 5;

        // when
        lineRestAssured.editLine(location, name, color, distance);

        // then
        lineAssert.assertEditLine(location, name, upStationId, downStationId, color, distance);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createLineResponse
                = lineRestAssured.createLine(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        String location = getLocation(createLineResponse);

        // when
        lineRestAssured.deleteLine(location);

        // then
        lineAssert.assertDeleteLine(location);
    }

    private static String getLocation(final ExtractableResponse<Response> createLineResponse) {
        return createLineResponse.header(HttpHeaders.LOCATION);
    }
}
