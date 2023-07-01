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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames.size()).isEqualTo(1);
        assertThat(stationNames.get(0)).isEqualTo("강남역");
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        Map<String, String> param1 = new HashMap<>();
        Map<String, String> param2 = new HashMap<>();

        param1.put("name", "강남역");
        param2.put("name", "서초역");

        ExtractableResponse<Response> response1 = RestAssured
                .given().log().all()
                .body(param1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .body(param2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        List<String> stationNames = RestAssured
                .when()
                .get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames.get(0)).isEqualTo("강남역");
        assertThat(stationNames.get(1)).isEqualTo("서초역");
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .when()
                .delete("/stations/1")
                .then().log().all()
                .extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<StationResponse> stationResponse = RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract().jsonPath().getList(".", StationResponse.class);

        assertThat(stationResponse.size()).isEqualTo(0);
    }
}
