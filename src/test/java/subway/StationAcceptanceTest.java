package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void before() {
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
        // when
        ExtractableResponse<Response> response = 지하철역_생성("강남역");
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        // then
        List<String> stationNames = 지하철역_목록_조회().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        //given
        지하철역_생성("창동역");
        지하철역_생성("노원역");
        //when
        ExtractableResponse<Response> response = 지하철역_목록_조회();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stations = response.jsonPath().getList("name", String.class);
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations).contains("노원역", "창동역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        //given
        final String 노원역 = "노원역";
        ExtractableResponse<Response> createResponse = 지하철역_생성(노원역);
        Long id = createResponse.jsonPath().getLong("id");
        //when
        ExtractableResponse<Response> deleteResponse = 지하철역_삭제(id);
        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        //then
        ExtractableResponse<Response> response = 지하철역_목록_조회();
        List<String> stations = response.jsonPath().getList("name", String.class);
        assertThat(stations.size()).isEqualTo(0);
        assertThat(stations).doesNotContain(노원역);
    }

    private ExtractableResponse<Response> 지하철역_삭제(Long id) {
        return RestAssured.given().log().all()
                .when().delete(String.format("/stations/%d", id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}