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
import subway.line.LineRequest;
import subway.station.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {
    Station s1 = new Station(1L, "강남역");
    Station s2 = new Station(2L, "삼성역");



    @BeforeEach
    public void setUp() {
        final String station1 = "강남역";
        final String station2 = "삼성역";

        createStation(station1);
        createStation(station2);
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

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void createLine() {

        // when
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", 1L, 2L, 10);
        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        // then
        List<Map<String, Object>> lineNames = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                //.statusCode(HttpStatus.OK.value())
                .extract().jsonPath().get();

        System.out.println("lineNames = " + lineNames);
    }
}
