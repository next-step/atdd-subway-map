package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    @DisplayName("지하철역 목록 조회")
    void getStationList(){
        //given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");

        ExtractableResponse<Response> response1 =
                RestAssured.given().log().all()
                        .body(params1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //given
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "삼성역");

        ExtractableResponse<Response> response2 =
                RestAssured.given().log().all()
                        .body(params2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @Test
    @DisplayName("지하철역 삭제")
    void deleteStation(){
        //given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");

        ExtractableResponse<Response> response1 =
                RestAssured.given().log().all()
                        .body(params1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //given
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "삼성역");

        ExtractableResponse<Response> response2 =
                RestAssured.given().log().all()
                        .body(params2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        List<Integer> stationIds =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("id", Integer.class);

        //then

        ExtractableResponse<Response> response3 =
                RestAssured.given()
                        .pathParam("id", stationIds.get(0))
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/stations/{id}")
                        .then().log().all()
                        .extract();
        assertThat(response3.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<Integer> stationNames3 =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("id", Integer.class);

        assertThat(stationNames3).doesNotContain(stationIds.get(0));
    }
}