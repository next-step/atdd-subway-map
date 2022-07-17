package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.client.LineClient;
import nextstep.subway.acceptance.client.StationClient;
import nextstep.subway.acceptance.util.JsonResponseConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    StationClient stationClient;

    @Autowired
    LineClient lineClient;

    @Autowired
    JsonResponseConverter responseConverter;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    @DirtiesContext
    void createLine() {
        // given
        String 이호선 = "2호선";
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(List.of("강남역", "역삼역")));

        // when
        lineClient.createLine(이호선, "green", stationIds.get(0), stationIds.get(1), 10);

        // then
        assertThat(responseConverter.convertToNames(lineClient.fetchLines())).contains(이호선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    @DirtiesContext
    void getLines() {
        // given
        int stationSize = 2;
        int lineSize = 2;
        String 이호선 = "2호선";
        String 신분당선 = "신분당선";
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(List.of("강남역", "역삼역", "양재역")));
        lineClient.createLine(이호선, "green", stationIds.get(0), stationIds.get(1), 10);
        lineClient.createLine(신분당선, "red", stationIds.get(0), stationIds.get(2), 7);

        // when
        ExtractableResponse<Response> linesResponse = lineClient.fetchLines();

        // then
        assertThat(responseConverter.convertToList(linesResponse, "stations"))
                .allMatch(stations -> stations.size() == stationSize);
        assertThat(responseConverter.convertToNames(linesResponse))
                .hasSize(lineSize)
                .containsExactly(이호선, 신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    @DirtiesContext
    void getLine() {
        // given
        String 이호선 = "2호선";
        List<String> stations = List.of("강남역", "역삼역");
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(stations));
        Long lineId = responseConverter.convertToId(lineClient.createLine(이호선, "green", stationIds.get(0), stationIds.get(1), 10));

        // when
        ExtractableResponse<Response> response = lineClient.fetchLine(lineId);

        // then
        assertThat(responseConverter.convert(response, "stations", List.class))
                .hasSize(stations.size());
        assertThat(responseConverter.convertToName(response)).isEqualTo(이호선);
    }

    /**
    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    @DirtiesContext
    void modifyLine() {
        // given
        String 이호선 = "2호선";
        String 새로운이호선 = "새로운2호선";
        String green = "green";
        String coreGreen = "coreGreen";
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(List.of("강남역", "역삼역")));
        Long lineId = responseConverter.convertToId(lineClient.createLine(이호선, green, stationIds.get(0), stationIds.get(1), 10));

        // when
        lineClient.modifyLine(lineId, 새로운이호선, coreGreen);

        // then
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .containsExactly(새로운이호선);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    @DirtiesContext
    void deleteLine() {
        // given
        String 이호선 = "2호선";
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(List.of("강남역", "역삼역")));
        Long lineId = responseConverter.convertToId(lineClient.createLine(이호선, "green", stationIds.get(0), stationIds.get(1), 10));

        // when
        lineClient.deleteLine(lineId);

        // then
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .doesNotContain(이호선);
    }

}