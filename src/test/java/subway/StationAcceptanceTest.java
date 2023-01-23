package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
        postStation("강남역").statusCode(HttpStatus.CREATED.value());

        given().log().all()
        .when()
                .get("/stations")
        .then().log().all()
                .body("name", hasItems("강남역"));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 2개를 생성하고 목록을 조회한다.")
    @Test
    void createStations() {
        List.of("강남역", "교대역")
                .forEach(this::postStation);

        given().log().all()
        .when()
                .get("/stations")
        .then().log().all()
                .body("size()", equalTo(2))
                .body("name", contains("강남역", "교대역"));
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 2개를 생성하고 그중 1개를 삭제한다.")
    @Test
    void deleteStation() {
        postStation("교대역");
        ExtractableResponse<Response> createResponse = postStation("강남역").extract();

        given().log().all().
        when()
                .delete(createResponse.header("location")).
        then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given().log().all().
        when()
                .get("/stations").
        then().log().all()
                .body("size()", equalTo(1))
                .body("name", not(contains("강남역")));
    }

    private ValidatableResponse postStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then().log().all();
    }
}
