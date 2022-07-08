package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when & then 지하철역을 생성하면 지하철역이 생성된다
        ExtractableResponse<Response> 강남역_생성 = 지하철역_생성("강남역");
        assertThat(강남역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());


        // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        ExtractableResponse<Response> 지하철역_조회 = 지하철역_조회();
        assertThat(지하철역_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 지하철역_이름_조회(지하철역_조회);
        assertThat(stationNames).containsAnyOf("강남역");
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStation() {
        // given 2개의 지하철역 생성하고
        ExtractableResponse<Response> 신림역_생성 = 지하철역_생성("신림역");
        assertThat(신림역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> 신도림역_생성 = 지하철역_생성("신도림역");
        assertThat(신도림역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 지하철역 목록을 조회하면
        ExtractableResponse<Response> 지하철역_조회 = 지하철역_조회();
        assertThat(지하철역_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 지하철역_이름_조회(지하철역_조회);
        // then 생성한 두개의 지하철역을 응답 받는다.
        assertAll(
            () -> assertThat(stationNames).hasSize(2),
            () -> assertThat(stationNames).contains("신림역"),
            () -> assertThat(stationNames).contains("신도림역")
        );
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철 역을 제거한다.")
    void deleteStation() {
        // given 지하철역을 생성하고
        ExtractableResponse<Response> 신림역_생성 = 지하철역_생성("신림역");
        assertThat(신림역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 그 지하철역을 삭제하면
        String 신림역_ID = 신림역_생성.jsonPath().getString("id");
        ExtractableResponse<Response> 지하철역_삭제 = 지하철역_삭제(신림역_ID);
        assertThat(지하철역_삭제.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        ExtractableResponse<Response> 지하철역_조회 = 지하철역_조회();
        assertThat(지하철역_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 지하철역_이름_조회(지하철역_조회);
        assertThat(stationNames).doesNotContain("신림역");
    }

    private List<String> 지하철역_이름_조회(ExtractableResponse<Response> response) {
        return response
            .jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> 지하철역_삭제(String id) {
        return RestAssured.given().log().all()
            .when().delete("/stations/" + id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> station = new HashMap<>();
        station.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(station)
            .when().post("/stations")
            .then().log().all()
            .extract();
        return response;
    }

    private ExtractableResponse<Response> 지하철역_조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
        return response;
    }

}
