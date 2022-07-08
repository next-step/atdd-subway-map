package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
    public void setUp() {
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
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test()
    void getStations() {
        // given 2개의 지하철역을 생성하고
        Map<String, String> body1 = new HashMap<>();
        body1.put("name", "영통역");
        RestAssured.given().log().all()
                   .body(body1)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().log().all()
                   .post("/stations")
                   .then().log().all();

        Map<String, String> body2 = new HashMap<>();
        body2.put("name", "선릉역");
        RestAssured.given().log().all()
                   .body(body2)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().log().all()
                   .post("/stations")
                   .then().log().all();

        // when 지하철역 목록을 조회하면
        List<String> names = RestAssured.given().log().all()
                                        .when()
                                        .get("/stations")
                                        .then().log().all()
                                        .extract().jsonPath().getList("name", String.class);

        // 생성한 2개의 지하철역을 응답 받는다.
        assertThat(names).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test()
    void deleteStation() {
        //1. 지하철역을 생성하고
        Map<String, String> body = new HashMap<>();
        body.put("name", "선릉역");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(body)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when().log().all()
                                                            .post("/stations")
                                                            .then().log().all()
                                                            .extract();

        //2. 지하철을 삭제하면
        RestAssured.given().log().all()
                   .pathParam("id", response.jsonPath().get("id"))
                   .when()
                   .delete("/stations/{id}")
                   .then().log().all();

        //3. 그 지하철 목록 조회 시 생성한 역을 찾을 수 없다.
        List<String> names = RestAssured.given().log().all()
                                        .when()
                                        .get("/stations")
                                        .then().log().all()
                                        .extract().jsonPath().getList("name", String.class);
        assertThat(names).isEmpty();

    }
}