package subway.line.acceptance;

import core.AcceptanceTestExtension;
import core.RestAssuredHelper;
import core.TestConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import subway.line.service.dto.LineResponse;
import subway.station.acceptance.StationAcceptanceTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(AcceptanceTestExtension.class)
public class LineAcceptanceTest {
    public static final String LINE_API_PATH = "/lines";
    private Long 지하철역_Id;
    private Long 새로운지하철역_Id;
    private Long 또다른지하철역_Id;

    @BeforeEach
    void setUp() {
        지하철역_Id = createStation("지하철역");
        새로운지하철역_Id = createStation("새로운지하철역");
        또다른지하철역_Id = createStation("또다른지하철역");
    }

    /**
     * When 노선을 생성하면
     * Then 노선이 생성된다
     * Then 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLineTest() {
        // when
        final ExtractableResponse<Response> response = requestCreateLine("신분당선", "bg-red-600", 지하철역_Id, 새로운지하철역_Id, 10);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            final LineResponse lineResponse = response.as(LineResponse.class);
            softly.assertThat(lineResponse.getName()).isEqualTo("신분당선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("bg-red-600");
            softly.assertThat(lineResponse.getStations())
                    .extracting("id").containsExactly(지하철역_Id, 새로운지하철역_Id);
        });

        // then
        final ExtractableResponse<Response> linesResponse = RestAssuredHelper.get(LINE_API_PATH);
        final List<String> lineNames = linesResponse.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 노선을 생성하고
     * When 노선 목록을 조회하면
     * Then 2개의 노선을 응답 받는다
     */
    @DisplayName("노선 목록을 조회한다.")
    @Test
    void fetchLinesTest() {
        // given
        final ExtractableResponse<Response> 신분당선_response = requestCreateLine("신분당선", "bg-red-600", 지하철역_Id, 새로운지하철역_Id, 10);
        final ExtractableResponse<Response> 분당선_response = requestCreateLine("분당선", "bg-green-600", 지하철역_Id, 또다른지하철역_Id, 10);

        // when
        final ExtractableResponse<Response> response = RestAssuredHelper.get(LINE_API_PATH);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                , () -> assertResponseData(response, RestAssuredHelper.getIdFrom(신분당선_response), "신분당선", 지하철역_Id, 새로운지하철역_Id)
                , () -> assertResponseData(response, RestAssuredHelper.getIdFrom(분당선_response), "분당선", 지하철역_Id, 또다른지하철역_Id)
        );
    }

    private void assertResponseData(final ExtractableResponse<Response> response, final Long id, final String name, final Long upStationId, final Long downStationId) {
        final LineResponse lineResponse = RestAssuredHelper.findObjectFrom(response, id, LineResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(lineResponse.getName()).isEqualTo(name);
            softly.assertThat(lineResponse.getStations())
                    .extracting("id").containsExactly(upStationId, downStationId);
        });
    }

    private Long createStation(final String name) {
        return RestAssuredHelper.getIdFrom(RestAssuredHelper.post(StationAcceptanceTest.STATION_API_PATH, Map.of("name", name)));
    }

    private ExtractableResponse<Response> requestCreateLine(final String name, final String color, final Long upStationId, final Long downStationId, final int distance) {
        final Map<String, Object> createLineRequest = createLineRequestFixture(name, color, upStationId, downStationId, distance);
        return RestAssuredHelper.post(LINE_API_PATH, createLineRequest);
    }

    private Map<String, Object> createLineRequestFixture(final String name, final String color, final Long upStationId, final Long downStationId, final int distance) {
        return Map.of(
                "name", name
                , "color", color
                , "upStationId", upStationId
                , "downStationId", downStationId
                , "distance", distance
        );
    }

}
