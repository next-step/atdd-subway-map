package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        ExtractableResponse<Response> response = createSubwayStation("강남역");

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
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        //given
        String station1 = "강남역";
        String station2 = "양재역";
        createSubwayStation(station1);
        createSubwayStation(station2);

        //when
        ExtractableResponse<Response> response = getAllStationsResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        List<String> names = response.jsonPath().getList("name", String.class);
        assertThat(names).containsExactly(station1, station2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역을 삭제한다.")
    void deleteStation() {
        // given
        String station1 = "강남역";
        String station2 = "양재역";
        ExtractableResponse<Response> subwayStation1 = createSubwayStation(station1);
        ExtractableResponse<Response> subwayStation2 = createSubwayStation(station2);

        // when
        Object deletedId = subwayStation1.jsonPath().get("id");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", deletedId)
                .delete("/stations/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> stationsResponse = getAllStationsResponse();

        List<String> names = stationsResponse.jsonPath().getList("name", String.class);
        assertThat(names).containsOnly("양재역");
    }


    /**
     * 지하철역 생성 메서드
     * @param stationName
     * @return
     */
    private static ExtractableResponse<Response> createSubwayStation(String stationName) {
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
     * 전체 지하철역 조회 메서드
     * @return
     */
    private static ExtractableResponse<Response> getAllStationsResponse() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
}