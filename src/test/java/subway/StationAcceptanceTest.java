package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
        ExtractableResponse<Response> response = createStation("강남역");

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

    private static ExtractableResponse<Response> createStation(final String name) {

        Map<String, String> params = new HashMap<>();

        params.put("name", name);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void showStations() {

        //given
        String name1 = "지하철역이름";
        String name2 = "새로운지하철역이름";
        String name3 = "또다른지하철역이";

        createStation(name1);
        createStation(name2);
        createStation(name3);

        //when
        ExtractableResponse<Response> response = getStationsList();

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("", StationResponse.class)).hasSize(3),
            () -> assertThat(response.jsonPath().getString("[0].name")).isEqualTo(name1),
            () -> assertThat(response.jsonPath().getString("[1].name")).isEqualTo(name2),
            () -> assertThat(response.jsonPath().getString("[2].name")).isEqualTo(name3)
        );
    }

    private static ExtractableResponse<Response> getStationsList() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역 삭제")
    void removeStation() {

        //given
        ExtractableResponse<Response> createResponse = createStation("새로운지하철역");

        //when
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/stations/{id}", createResponse.jsonPath().getLong("id"))
            .then().log().all()
            .assertThat().statusCode(HttpStatus.NO_CONTENT.value())
            .extract();

        //then
        ExtractableResponse<Response> stationsList = getStationsList();

        //then
        assertThat(stationsList.jsonPath().getList("")).isEmpty();

    }
}