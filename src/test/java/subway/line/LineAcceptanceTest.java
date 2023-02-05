package subway.line;

import static subway.line.LineAssert.노선_목록_조회_검증;
import static subway.line.LineAssert.노선_삭제_검증;
import static subway.line.LineAssert.노선_생성_검증;
import static subway.line.LineAssert.노선_수정_검증;
import static subway.line.LineAssert.노선_조회_검증;
import static subway.line.LineRestAssured.노선_목록_조회;
import static subway.line.LineRestAssured.노선_삭제;
import static subway.line.LineRestAssured.노선_생성;
import static subway.line.LineRestAssured.노선_수정;
import static subway.station.StationRestAssured.역_생성;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import subway.util.DatabaseCleanup;

@ActiveProfiles("acceptance")
@DisplayName("지하철 노선 관련 기능")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    private static final String LINE_NAME = "신분당선";
    private static final String COLOR = "bg-red-600";
    private static final int DISTANCE = 10;

    @LocalServerPort
    private int port;

    private Long upStationId;
    private Long downStationId;

    private final DatabaseCleanup databaseCleanup;

    public LineAcceptanceTest(final DatabaseCleanup databaseCleanup) {
        this.databaseCleanup = databaseCleanup;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        this.databaseCleanup.truncateTable();
        this.upStationId = 역_생성("강남역").jsonPath().getLong("id");
        this.downStationId = 역_생성("양재역").jsonPath().getLong("id");
    }

    @DisplayName("지하철 노선 생성한다.")
    @Test
    void createLine() {
        // when
        노선_생성(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        // then
        노선_생성_검증(1L, LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        노선_생성(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);
        long upStationId2 = 역_생성("신림역").jsonPath().getLong("id");
        long downStationId2 = 역_생성("노량진역").jsonPath().getLong("id");
        노선_생성("2호선", "bg-green-600", upStationId2, downStationId2, 20);

        // when
        var response = 노선_목록_조회();

        // then
        노선_목록_조회_검증(response, 2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        var createLineResponse = 노선_생성(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        String location = getLocation(createLineResponse);

        // when, then
        노선_조회_검증(LINE_NAME, COLOR, DISTANCE, location);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void editLine() {
        // given
        var createLineResponse = 노선_생성(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        String location = getLocation(createLineResponse);

        String name = "수정한 지하철 노선 이름";
        String color = "수정한 지하철 색상";
        int distance = 5;

        // when
        노선_수정(location, name, color, distance);

        // then
        노선_수정_검증(location, name, upStationId, downStationId, color, distance);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        var createLineResponse = 노선_생성(LINE_NAME, COLOR, upStationId, downStationId, DISTANCE);

        String location = getLocation(createLineResponse);

        // when
        노선_삭제(location);

        // then
        노선_삭제_검증(location);
    }

    private static String getLocation(final ExtractableResponse<Response> createLineResponse) {
        return createLineResponse.header(HttpHeaders.LOCATION);
    }
}
