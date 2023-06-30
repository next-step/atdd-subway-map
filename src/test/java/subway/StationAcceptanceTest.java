package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestDatabaseCleaner.class)
class StationAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestDatabaseCleaner dbCleaner;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        dbCleaner.cleanUpStation();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 1개 생성하면 목록 조회시 생성한 1개의 역이 조회된다.")
    @Test
    void createOneStation() {
        // when
        ExtractableResponse<Response> response = 지하철_생성("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철_목록_조회().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 2개 생성하면 목록 조회시 생성한 2개의 역이 조회된다.")
    @Test
    void createTwoStation() {
        // given
        지하철_생성("별똥별");
        지하철_생성("역삼역");

        // when
        List<String> stationNames = 지하철_목록_조회().jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsExactly("별똥별", "역삼역");
    }
    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 생성한 후 삭제하면 목록 조회시 해당 역이 조회되지 않는다.")
    @Test
    void create_and_deleteStation() {
        // given
        ExtractableResponse<Response> response = 지하철_생성("우영우");

        // when
        지하철_삭제(response.jsonPath().getLong("id"));

        // then
        List<String> stationNames = 지하철_목록_조회().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("우영우");
    }

    private ExtractableResponse<Response> 지하철_생성(String name){
        return RestAssured.given().log().all()
                .body(Collections.singletonMap("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_삭제(Long id){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }
}
