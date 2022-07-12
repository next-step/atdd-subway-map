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
        ExtractableResponse<Response> response = 지하철역을_생성한다("name", "강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(지하철역을_조회한다("name")).containsAnyOf("강남역");
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
        // given
        지하철역을_생성한다("name", "영통역");
        지하철역을_생성한다("name", "선릉역");

        // when
        List<String> names = 지하철역을_조회한다("name");

        // then
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
        // given
        ExtractableResponse<Response> response = 지하철역을_생성한다("name", "사당역");

        // when
        지하철역을_삭제한다(response.jsonPath().get("id"));

        // then
        assertThat(지하철역을_조회한다("name")).isEmpty();

    }


    private ExtractableResponse<Response> 지하철역을_생성한다(String id, String name) {
        Map<String, Object> body = Map.of(id, name);
        return RestAssured.given().log().all()
                          .body(body)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().log().all()
                          .post("/stations")
                          .then().log().all()
                          .extract();
    }

    private List<String> 지하철역을_조회한다(String id) {
        return RestAssured.given().log().all()
                          .when()
                          .get("/stations")
                          .then().log().all()
                          .extract()
                          .jsonPath().getList(id, String.class);
    }

    private void 지하철역을_삭제한다(Long id) {
        RestAssured.given().log().all()
                   .pathParam("id", id)
                   .when()
                   .delete("/stations/{id}")
                   .then().log().all();
    }


}