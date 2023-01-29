package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.Colors;
import subway.common.Endpoints;
import subway.station.StationResponse;
import subway.utils.JsonBodyParam;
import subway.utils.RestAssuredClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        ExtractableResponse<Response> 강남역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "강남역").toMap());
        ExtractableResponse<Response> 서울대입구역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "서울대입구역").toMap());

        // when 지하철 노선을 생성하면
        long 강남역_아이디 = 강남역_생성_응답.jsonPath().getLong("id");
        long 서울대입구역_아이디 = 서울대입구역_생성_응답.jsonPath().getLong("id");

        var response = RestAssuredClient.post(
                Endpoints.LINES,
                new LineRequest(
                        "신분당선",
                        Colors.RED,
                        강남역_아이디,
                        서울대입구역_아이디,
                        10L
                )
        );
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
        List<LineResponse> lineResponses = RestAssuredClient.get(Endpoints.LINES).jsonPath().getList(".", LineResponse.class);
        assertThat(lineResponses).hasSize(1);

        var lineResponse = lineResponses.get(0);
        assertThat(lineResponse.getName()).isEqualTo("신분당선");
        assertThat(lineResponse.getColor()).isEqualTo(Colors.RED);
        assertThat(lineResponse.getStations()).hasSize(2);
        assertThat(lineResponse.getStations().stream().map(StationResponse::getId))
                .containsExactly(
                        강남역_아이디,
                        서울대입구역_아이디
                );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("여러 개의 지하철 노선을 조회한다.")
    @Test
    void findAllLines() {
        ExtractableResponse<Response> 강남역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "강남역").toMap());
        ExtractableResponse<Response> 서울대입구역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "서울대입구역").toMap());

        // Given 지하철 노선을 생성하고
        long 강남역_아이디 = 강남역_생성_응답.jsonPath().getLong("id");
        long 서울대입구역_아이디 = 서울대입구역_생성_응답.jsonPath().getLong("id");

        var 신분당선_생성 = RestAssuredClient.post(
                Endpoints.LINES,
                new LineRequest(
                        "신분당선",
                        Colors.RED,
                        강남역_아이디,
                        서울대입구역_아이디,
                        10L
                )
        );
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        var 이호선_생성 = RestAssuredClient.post(
                Endpoints.LINES,
                new LineRequest(
                        "2호선",
                        Colors.BLUE,
                        강남역_아이디,
                        서울대입구역_아이디,
                        10L
                )
        );
        assertThat(이호선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // When 지하철 노선 목록을 조회하면
        List<LineResponse> lineResponses = RestAssuredClient.get(Endpoints.LINES).jsonPath().getList(".", LineResponse.class);

        // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(lineResponses).hasSize(2);
        var lineNames = lineResponses.stream().map(LineResponse::getName).collect(Collectors.toList());
        assertThat(lineNames).containsExactly("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("특정 지하철 노선을 조회한다.")
    @Test
    void findLineById() {
        ExtractableResponse<Response> 강남역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "강남역").toMap());
        ExtractableResponse<Response> 서울대입구역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "서울대입구역").toMap());

        // Given 지하철 노선을 생성하고
        long 강남역_아이디 = 강남역_생성_응답.jsonPath().getLong("id");
        long 서울대입구역_아이디 = 서울대입구역_생성_응답.jsonPath().getLong("id");

        String lineName = "신분당선";
        String color = Colors.RED;
        long upStationId = 강남역_아이디;
        long downStationId = 서울대입구역_아이디;

        var 신분당선_생성 = RestAssuredClient.post(
                Endpoints.LINES,
                new LineRequest(
                        lineName,
                        color,
                        upStationId,
                        downStationId,
                        10L
                )
        );
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // When 생성한 지하철 노선을 조회하면
        var path = 신분당선_생성.header("Location");
        var 신분당선_조회 = RestAssuredClient.get(path);
        var 신분당선_응답 = 신분당선_조회.as(LineResponse.class);

        // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertThat(신분당선_응답.getName()).isEqualTo(lineName);
        assertThat(신분당선_응답.getColor()).isEqualTo(color);
        assertThat(신분당선_응답.getStations().stream().map(StationResponse::getId)).containsExactly(upStationId, downStationId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("특정 지하철 노선을 수정한다.")
    @Test
    void updateLineById() {
        ExtractableResponse<Response> 강남역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "강남역").toMap());
        ExtractableResponse<Response> 서울대입구역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "서울대입구역").toMap());

        // Given 지하철 노선을 생성하고
        long 강남역_아이디 = 강남역_생성_응답.jsonPath().getLong("id");
        long 서울대입구역_아이디 = 서울대입구역_생성_응답.jsonPath().getLong("id");

        String lineName = "신분당선";
        String color = Colors.RED;
        long upStationId = 강남역_아이디;
        long downStationId = 서울대입구역_아이디;

        var 신분당선_생성 = RestAssuredClient.post(
                Endpoints.LINES,
                new LineRequest(
                        lineName,
                        color,
                        upStationId,
                        downStationId,
                        10L
                )
        );
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // When 생성한 지하철 노선을 수정하면
        var path = 신분당선_생성.header("Location");

        String updateLineName = "4호선";
        String updateColor = Colors.GREEN;
        var updateLineResponse = RestAssuredClient.put(path, new UpdateLineRequest(
                updateLineName,
                updateColor
        ));

        // Then line is updated.
        var 사호선_응답 = updateLineResponse.as(LineResponse.class);
        assertThat(사호선_응답.getName()).isEqualTo(updateLineName);
        assertThat(사호선_응답.getColor()).isEqualTo(updateColor);
        assertThat(사호선_응답.getStations().stream().map(StationResponse::getId)).containsExactly(upStationId, downStationId);
    }
}
