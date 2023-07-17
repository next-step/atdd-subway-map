package subway;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineRequest;
import subway.station.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LineAcceptanceTest {
    Station s1 = new Station(1L, "강남역");
    Station s2 = new Station(2L, "삼성역");



    @BeforeEach
    public void setUp() {

    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void createLine() {
        final String station1 = "강남역";
        final String station2 = "삼성역";

        createStation(station1);
        createStation(station2);

        // when
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", 1L, 2L, 10);
        createLineRequest(lineRequest);


        // then
        List<Map<String, Object>> lineNames = getList();

        System.out.println("lineNames = " + lineNames);
        Assertions.assertThat(lineNames.get(0).get("name")).isEqualTo("2호선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @Test
    void findAllLines() {
        // given
        final String station1 = "강남역";
        final String station2 = "삼성역";

        createStation(station1);
        createStation(station2);

        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", 1L, 2L, 10);
        createLineRequest(lineRequest);

        lineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10);
        createLineRequest(lineRequest);

        // when
        List<Map<String, Object>> lineNames = getList();
        // then
        Assertions.assertThat(lineNames.get(0)).containsEntry("name", "2호선");
        Assertions.assertThat(lineNames.get(1)).containsEntry("name", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선을 조회하면
     * Then 해당 노선이 조회된다
     */
    @Test
    void findLine() {
        // given
        final String station1 = "강남역";
        final String station2 = "삼성역";

        createStation(station1);
        createStation(station2);

        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", 1L, 2L, 10);
        createLineRequest(lineRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/1")
                .then().log().all()
                .extract();

        // then
        Assertions.assertThat((String) response.jsonPath().get("name")).isEqualTo("2호선");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선 목록을 수정하면
     * Then 해당 노선은 수정된다
     */
    @Test
    void modifyLines() {

        // given
        final String station1 = "강남역";
        final String station2 = "삼성역";

        createStation(station1);
        createStation(station2);

        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", 1L, 2L, 10);
        createLineRequest(lineRequest);

        // when
        lineRequest = new LineRequest("3호선", "bg-green-600");
        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        // then
        List<Map<String, Object>> lineNames = getList();

        Assertions.assertThat(lineNames.get(0)).containsEntry("name", "3호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void deleteLine() {

        // given
        final String station1 = "강남역";
        final String station2 = "삼성역";

        createStation(station1);
        createStation(station2);

        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", 1L, 2L, 10);
        createLineRequest(lineRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/1")
                .then().log().all()
                .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    private ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static List<Map<String, Object>> getList() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().get();
    }

    private static void createLineRequest(LineRequest lineRequest) {
        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }




}
