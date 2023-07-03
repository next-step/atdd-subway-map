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

    @DisplayName("지하철역을 1개 생성하면 목록 조회시 생성한 1개의 역이 조회된다.")
    @Test
    void createOneStation() {
        // when
        ExtractableResponse<Response> response = 지하철_생성("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        지하철_목록_조회됨("강남역");
    }

    @DisplayName("지하철역을 2개 생성하면 목록 조회시 생성한 2개의 역이 조회된다.")
    @Test
    void createTwoStation() {
        // given
        지하철_생성("별똥별");
        지하철_생성("역삼역");

        // when & then
        지하철_목록_조회됨("별똥별", "역삼역");
    }

    @DisplayName("지하철역을 생성한 후 삭제하면 목록 조회시 해당 역이 조회되지 않는다.")
    @Test
    void create_and_deleteStation() {
        // given
        ExtractableResponse<Response> response = 지하철_생성("우영우");

        // when
        지하철_삭제(response.jsonPath().getLong("id"));

        // then
        지하철_목록_조회_안됨("우영우");
    }

    private ExtractableResponse<Response> 지하철_생성(String name){
        return RestAssured.given().log().all()
                .body(Collections.singletonMap("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
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

    private void 지하철_목록_조회됨(String... expected) {
        List<String> results = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract()
                .jsonPath().getList("name", String.class);

        assertThat(results.size()).isEqualTo(expected.length);
        assertThat(results).containsExactly(expected);
    }

    private void 지하철_목록_조회_안됨(String... expected) {
        List<String> results = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract()
                .jsonPath().getList("name", String.class);

        assertThat(results).doesNotContain(expected);
    }
}
