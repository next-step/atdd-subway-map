package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.stations.StationRestAssured;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private ExtractableResponse<Response> stationResponse1;
    private ExtractableResponse<Response> stationResponse2;

    @BeforeEach
    void stationCreate() {
        // given - 역 생성
        Map<String, String> station1 = new HashMap<>();
        station1.put("name", "논현역");
        stationResponse1 = StationRestAssured.createStation(station1);

        Map<String, String> station2 = new HashMap<>();
        station2.put("name", "강남역");
        stationResponse2 = StationRestAssured.createStation(station2);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createRoute() {
        // given - 노선 생성
        LineRequest lineRequest = new LineRequest(
                "신분당선",
                "red",
                stationResponse1.jsonPath().getLong("id"),
                stationResponse2.jsonPath().getLong("id"),
                10L);

        // when
        ExtractableResponse<Response> response = LineRestAssured.createRoute(lineRequest);

        // then - 노선 목록
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> {
                    ExtractableResponse<Response> readLines = LineRestAssured.readLines();
                    assertThat(readLines.jsonPath().getList("name")).contains(lineRequest.getName());
                }
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void searchRouteList() {
        // given
        LineRequest lineRequest1 = new LineRequest(
                "신분당선",
                "red",
                stationResponse1.jsonPath().getLong("id"),
                stationResponse2.jsonPath().getLong("id"),
                10L);

        LineRequest lineRequest2 = new LineRequest(
                "강남 2호선",
                "green",
                stationResponse1.jsonPath().getLong("id"),
                stationResponse2.jsonPath().getLong("id"),
                20L);

        LineRestAssured.createRoute(lineRequest1);
        LineRestAssured.createRoute(lineRequest2);

        // when
        ExtractableResponse<Response> response = LineRestAssured.readLines();

        // then
        List<String> lineNames = response.jsonPath().getList("name");
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineNames).containsExactly(lineRequest1.getName(), lineRequest2.getName())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void searchRoute() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void patchRoute() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteRoute() {

    }
}
