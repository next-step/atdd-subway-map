package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest extends AcceptanceTest {

    @AfterEach
    void cleanUp() {
        final ExtractableResponse<Response> 지하철역_목록_응답 = 지하철역_목록_조회요청();
        final List<Long> ids = 지하철역_목록_응답.jsonPath().getList("id", Long.class);
        ids.forEach(this::지하철역_삭제요청);
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

        ExtractableResponse<Response> response = 지하철역_생성요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_목록_조회요청()
                .jsonPath()
                .getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "역삼역");
        지하철역_생성요청(params1);

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "선릉역");
        지하철역_생성요청(params2);

        // when
        final ExtractableResponse<Response> response = 지하철역_목록_조회요청();

        // then
        final List<String> names = response
                .jsonPath()
                .getList("name", String.class);
        assertThat(names).contains("역삼역", "선릉역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "삼성역");
        final ExtractableResponse<Response> 삼성역 = 지하철역_생성요청(params);

        // when
        final long id = 삼성역.jsonPath().getLong("id");
        지하철역_삭제요청(id);

        // then
        final ExtractableResponse<Response> getResponse = 지하철역_목록_조회요청();
        final List<String> names = getResponse
                .jsonPath()
                .getList("name", String.class);
        assertThat(names).doesNotContain("삼성역");
    }

    public static ExtractableResponse<Response> 지하철역_생성요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_목록_조회요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제요청(long id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

}