package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

        ExtractableResponse<Response> response = RestAssured
                        .given().log().all()
                            .body(params)
                            .port(port)
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
                            .port(port)
                        .when()
                            .get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("2개의 지하철역을 생성하고 지하철역 목록을 조회해서 2개의 지하철역을 응답 받는다.")
    @Test
    void findTwoStationsTest() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response1 = RestAssured
                .given().log().all()
                    .port(port)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                .when()
                    .post("/stations")
                .then().log().all()
                .extract();

        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        params.put("name", "선릉역");

        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                    .port(port)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                .when()
                    .post("/stations")
                .then().log().all()
                .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<String> stationList = RestAssured
                .given().log().all()
                    .port(port)
                .when()
                    .get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        //then
        assertThat(stationList).containsAll(List.of("강남역", "선릉역"));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성

}