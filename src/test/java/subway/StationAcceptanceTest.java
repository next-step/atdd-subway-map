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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName ("지하철역 관련 기능")
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName ("지하철역을 생성한다.")
    @Test
    void createStation () {
        ExtractableResponse<Response> response = createSubwayStation("신림역");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName ("지하철역을 조회한다")
    @Test
    void readStation () {

        //given
        final String first_station = "신림역";
        final String second_station = "봉천역";
        createSubwayStation(first_station);
        createSubwayStation(second_station);


        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).containsExactly("신림역" , "봉천역");

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName ("역을 삭제한다")
    @Test
    void deleteStation () {

        //given
        long stationId = createSubwayStation("사상역").jsonPath().getLong("id");
        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .basePath("/stations")
                .pathParam("id" , stationId)
                .when().delete("{id}")
                .then().log().all()
                .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    private ExtractableResponse<Response> createSubwayStation (String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name" , stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

}