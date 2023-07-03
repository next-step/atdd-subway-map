package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
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
    private final static int PORT = 8080;
    private final static String MISA = "미사역";
    private final static String GANGIL = "강일역";

    @BeforeEach
    void setUp() {
        RestAssured.port = PORT;
    }
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
    @DisplayName("지하철역 2개 생성")
    @Test
    void createStations() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", MISA);
        ExtractableResponse<Response> stationByName = createStationByName(params);
        assertThat(stationByName.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        params.replace("name", GANGIL);
        ExtractableResponse<Response> stationByName2 = createStationByName(params);
        assertThat(stationByName2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        //when
        List<String> names = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(names).containsExactly(MISA, GANGIL);
    }

    private ExtractableResponse<Response> createStationByName(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = new HashMap<>();

        params.put("name", MISA);
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        long id = response.jsonPath().getLong("id");

        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
