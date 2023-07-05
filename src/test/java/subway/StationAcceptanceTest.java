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
    private int serverPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
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
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void getStations() {
        //given
        주어진_이름으로_지하철역을_생성한다("강남역");
        주어진_이름으로_지하철역을_생성한다("판교역");

        //when
        ExtractableResponse<Response> stations = 지하철역_목록을_조회한다();

        //then
        List<String> stationNames = stations.jsonPath().getList("name");

        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).contains("강남역");
        assertThat(stationNames).contains("판교역");
    }

    void 주어진_이름으로_지하철역을_생성한다(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all();
    }

    ExtractableResponse<Response> 지하철역_목록을_조회한다() {
        return RestAssured.given().log().all()
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
    @DisplayName("지하철역을 목록에서 삭제한다")
    @Test
    void deleteStation() {
        //given
        주어진_이름으로_지하철역을_생성한다("강남역");
        Object stationId = 지하철역_목록을_조회한다().jsonPath().getList("id").get(0);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(지하철역이_존재하지_않는다()).isTrue();
    }

    Boolean 지하철역이_존재하지_않는다() {
        return 지하철역_목록을_조회한다().jsonPath().getList("").size() == 0;
    }
}
