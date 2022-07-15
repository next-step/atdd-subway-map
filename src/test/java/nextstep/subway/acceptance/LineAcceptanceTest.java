package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("노선 관련 인수 테스트")
class LineAcceptanceTest extends BaseAcceptance {

    @BeforeEach
    void setUp() {
        RestAssured.port = super.port;
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
        //when
        final long id = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        //then
        final ExtractableResponse<Response> lineResponse = findApplicableLine(id);

        final String name = lineResponse.jsonPath().getString("name");
        assertThat(name).isEqualTo("신분당선");
    }

    /*
    Given 2개의 지하철 노선을 생성하고
    When 지하철 노선 목록을 조회하면
    Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철 노선 목록 조회")
    void getLines() {
        지하철_노선_생성("강남역", "양재역", "신분당선", "red");
        지하철_노선_생성("까치울역", "온수역", "7호선", "green");

        //when
        final ExtractableResponse<Response> getLinesResponse = findAllLines();

        //then
        final List<String> name = getLinesResponse.jsonPath().getList("name", String.class);
        final List<List> stations = getLinesResponse.jsonPath().getList("stations", List.class);
        assertAll(
            () -> assertThat(name).hasSize(2)
                .containsExactly("신분당선", "7호선"),

            () -> assertThat(stations.get(0)).isEqualTo(List.of(
                Map.of("id", 1, "name", "강남역"),
                Map.of("id", 2, "name", "양재역")
            )),

            () -> assertThat(stations.get(1)).isEqualTo(List.of(
                Map.of("id", 3, "name", "까치울역"),
                Map.of("id", 4, "name", "온수역")
            ))
        );
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
        final long id = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        //when
        final ExtractableResponse<Response> getLinesResponse = findApplicableLine(id);

        //then
        String lineName = getLinesResponse.jsonPath().get("name");
        String startStation = getLinesResponse.jsonPath().get("stations[0].name");
        String endStation = getLinesResponse.jsonPath().get("stations[1].name");

        assertAll(
            () -> assertThat(lineName).isEqualTo("신분당선"),
            () -> assertThat(startStation).isEqualTo("강남역"),
            () -> assertThat(endStation).isEqualTo("양재역")
        );
    }

    /*
    Given 지하철 노선을 생성하고
    When 생성한 지하철 노선을 수정하면
    Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철 노선 수정")
    void updateLines() {
        final long id = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        //when
        modifyLine();

        //then
        final ExtractableResponse<Response> getLinesResponse = findApplicableLine(id);
        assertThat(getLinesResponse.jsonPath().getString("name")).isEqualTo("다른분당선");
        assertThat(getLinesResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
    }

    private void modifyLine() {
        final Map<String, String> param = Map.of("name", "다른분당선", "color", "bg-red-600");
        RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/1")
            .then().log().all();
    }

    /*
    Given 지하철 노선을 생성하고
    When 생성한 지하철 노선을 삭제하면
    Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철 노선 삭제")
    void removeLines() {
        //given
        final long id = 지하철_노선_생성("강남역", "양재역", "신분당선", "red");

        //when
        final ExtractableResponse<Response> removeResponse = removeLine(id);

        //then
        assertThat(removeResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> removeLine(final long id) {
        return RestAssured.given().log().all()
            .when().delete("/lines/" + id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> findApplicableLine(final long id) {
        return RestAssured.given().log().all()
            .when().get("/lines/" + id)
            .then().log().all()
            .extract();
    }
}
