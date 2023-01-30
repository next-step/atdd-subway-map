package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
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

    @DisplayName("지하철역을 조회한다.")
    @Test
    void readStations() {
        // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
        final String firstStationName = "마천역";
        final String secondStationName = "잠실나루역";

        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", firstStationName);

        ExtractableResponse<Response> firstCreateResponse = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();
        Assertions.assertThat(firstCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        params.put("name", secondStationName);

        ExtractableResponse<Response> secondCreateResponse = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();
        Assertions.assertThat(secondCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> readResponse = RestAssured
                .given()
                .when()
                .get("/stations")
                .then()
                .extract();

        List<String> stationNames = readResponse.body().jsonPath().get("name");

        //then
        Assertions.assertThat(stationNames.containsAll(List.of(firstStationName, secondStationName)));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // TODO: 지하철역 제거 인수 테스트 메서드 생성

        final String stationName = "금청구청역";

        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> createResponse = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();
        Assertions.assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Integer id = createResponse.body().jsonPath().get("id");

        //when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given()
                .when()
                .delete("/stations/" + id)
                .then()
                .extract();

        Assertions.assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> readResponse = RestAssured
                .given()
                .when()
                .get("/stations")
                .then()
                .extract();

        Assertions.assertThat(readResponse.body().jsonPath().getList("name")).doesNotContain(stationName);
    }
}
