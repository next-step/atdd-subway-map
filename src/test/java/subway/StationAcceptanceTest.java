package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @DisplayName("지하철역 생성후 목록 조회 테스트")
    @Test
    void selectStations() {
        //given
        List<String> stationNames = List.of("강남역", "역삼역");
        for (String stationName : stationNames) {
            StationRequest stationRequest = new StationRequest();
            stationRequest.setName(stationName);

            ExtractableResponse<Response> createResponse =
                    RestAssured.given().body(stationRequest)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/stations")
                            .then().extract();
            assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        //when
        ExtractableResponse<Response> selectResponse = RestAssured
                .given()
                .when().get("/stations")
                .then().extract();

        List<StationResponse> stations = selectResponse.jsonPath().getList("$..", StationResponse.class);

        //then
        assertThat(selectResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.stream().map(StationResponse::getName).collect(Collectors.toList())).allMatch(stations::contains);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역 생성후 지하철역 삭제 테스트")
    @Test
    void deleteStation() {
        //given
        String stationName = "봉천역";
        StationRequest stationRequest = new StationRequest();
        stationRequest.setName(stationName);

        ExtractableResponse<Response> createResponse =
                RestAssured.given().body(stationRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        long id = createResponse.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().pathParam("id", id)
                .when().delete("/stations/{id}")
                .then().extract();

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> selectResponse = RestAssured
                .given()
                .when().get("/stations")
                .then().extract();

        List<StationResponse> stations = selectResponse.jsonPath().getList("$..", StationResponse.class);

        assertThat(stations.stream().map(StationResponse::getId)).noneMatch(p -> id == p);
    }
}