package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
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
    void test_createStation() {
        // given
        String 강남역 = "강남역";

        // when
        ExtractableResponse<Response> response = createStation(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = showStations().jsonPath()
                .getList("name", String.class);
        assertThat(stationNames).containsAnyOf(강남역);
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("2개의 지하철역을 조회한다.")
    @Test
    void test_showStations() {
        // given
        String 강남역 = "강남역";
        String 서초역 = "서초역";
        createStation(강남역);
        createStation(서초역);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().extract();

        // then
        List<String> stationNames =
                showStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAll(Arrays.asList(강남역, 서초역));
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void test_deleteStation() {
        // given
        String 강남역 = "강남역";
        long id = createStation(강남역).jsonPath()
                .getLong("id");

        // when
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", id)
                .then().extract();

        // then
        List<String> stationNames =
                showStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).doNotHave(new Condition<>(s -> s.equals(강남역), 강남역 + "이 조회되었습니다"));
    }

    private static ExtractableResponse<Response> createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();
        return response;
    }

    private static ExtractableResponse<Response> showStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
}