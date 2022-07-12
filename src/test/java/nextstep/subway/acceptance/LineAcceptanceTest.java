package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import nextstep.subway.config.DataBaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
class LineAcceptanceTest {

    final StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();

    @LocalServerPort
    int port;

    @Autowired
    DataBaseCleaner dataBaseCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        dataBaseCleaner.afterPropertiesSet();
        dataBaseCleaner.tableClear();
    }

    /*
    When 지하철 노선을 생성하면
    Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선 생성")
    void createLines() {
        stationAcceptanceTest.createSubwayStation("강남역");
        stationAcceptanceTest.createSubwayStation("양재역");

        //when
        createSubwayLines("신분당선", "bg-red-600", 1, 2, 10);

        //then
        final ExtractableResponse<Response> lineResponse = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        final List<String> name = lineResponse.jsonPath().getList("name", String.class);
        assertThat(name).containsExactly("신분당선");
    }

    /*
    Given 2개의 지하철 노선을 생성하고
    When 지하철 노선 목록을 조회하면
    Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철 노선 목록 조회")
    void getLines() {
        stationAcceptanceTest.createSubwayStation("까치울역");
        stationAcceptanceTest.createSubwayStation("사당역");
        stationAcceptanceTest.createSubwayStation("판교역");
        //give
        createSubwayLines("신분당선", "bg-red-600", 1, 2, 10);
        createSubwayLines("분당선", "bg-red-600", 1, 3, 10);

        //when
        final ExtractableResponse<Response> getLinesResponse = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        //then
        final List<String> name = getLinesResponse.jsonPath().getList("name", String.class);
        assertThat(name).hasSize(2)
            .containsExactly("신분당선", "분당선");

    }

    /*
    Given 지하철 노선을 생성하고
    When 생성한 지하철 노선을 조회하면
    Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철 노선 조회")
    void getOneLine() {
        //given
        stationAcceptanceTest.createSubwayStation("합정역");
        stationAcceptanceTest.createSubwayStation("신촌역");

        createSubwayLines("2호선", "bg-green-600", 1, 2, 10);

        //when
        final ExtractableResponse<Response> getLinesResponse = RestAssured.given().log().all()
            .when().get("/lines/1")
            .then().log().all()
            .extract();

        //then
        String name = getLinesResponse.jsonPath().get("name");
        assertThat(name).isEqualTo("2호선");
    }

    private void createSubwayLines(final String name, final String color,
        final long upStationId, final long downStationId, final int distance) {
        final Map<String, Object> param = Map.of(
            "name", name,
            "color", color,
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance
        );

        RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then();
    }
}
