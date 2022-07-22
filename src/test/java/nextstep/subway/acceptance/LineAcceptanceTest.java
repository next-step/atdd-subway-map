package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private StationAcceptanceTest stationAcceptanceTest;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        stationAcceptanceTest = new StationAcceptanceTest();
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        //given
        StationResponse 지하철역 = stationAcceptanceTest.지하철역_생성("지하철역").body().as(StationResponse.class);
        StationResponse 새로운지하철역 = stationAcceptanceTest.지하철역_생성("새로운지하철역").body().as(StationResponse.class);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", 지하철역.getId());
        params.put("downStationId", 새로운지하철역.getId());
        params.put("distance", 10);

        //when
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then()
                .log().all()
                .extract();


        //then
        assertThatStatus(lineResponse, HttpStatus.CREATED);
        assertThat(lineResponse.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(lineResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(lineResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(lineResponse.jsonPath().getList("stations").size()).isEqualTo(2);

        ExtractableResponse<Response> linesResponse = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then()
                .log().all()
                .extract();
        assertThatStatus(linesResponse, HttpStatus.OK);
        assertThat(linesResponse.jsonPath().getList("lines").size()).isEqualTo(1);

    }

    private void assertThatStatus(ExtractableResponse<Response> response, HttpStatus expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus.value());
    }
}
