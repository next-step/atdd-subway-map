package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 2개를 생성하고, 생성된 2개의 지하철역을 확인한다.")
    @Test
    void createTwoStation() {
        // given
        String createStation1 = "강남역";
        String createStation2 = "양재역";

        createStation(createStation1);
        createStation(createStation2);

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        List<String> stationNames = response.jsonPath().getList("name", String.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames).containsAnyOf(createStation1, createStation2);
    }

    private Long createStation(String stationName) {
        HashMap<String, String> param = new HashMap<>();
        param.put("name", stationName);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(param)
            .when().post("/stations")
            .then().log().all()
            .extract().jsonPath().getLong("id");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("생성된 지하철역을 삭제 한다.")
    @Test
    void deleteStation() {
        // given
        String createStation = "양재역";
        Long stationId = createStation(createStation);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(String.format("%s/%d", "/stations", stationId))
            .then().log().all()
            .extract();

        String uri = String.format("%s/%d", "/stations", stationId);
        List<Long> stationIds = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(uri)
            .then().log().all()
            .extract().jsonPath().getList("id", Long.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(stationIds).isEmpty();
    }
}
