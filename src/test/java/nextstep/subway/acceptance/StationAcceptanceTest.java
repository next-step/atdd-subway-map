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
        ExtractableResponse<Response> response = 지하철역_생성요청("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        지하철역_목록에_포함되어있다("강남역");
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
        지하철역_생성요청("역삼역");
        지하철역_생성요청("선릉역");

        // when
        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_조회요청();

        // then
        지하철역_목록에_포함되어있다("역삼역", "선릉역");
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
        final ExtractableResponse<Response> 삼성역 = 지하철역_생성요청("삼성역");

        // when
        final long id = 삼성역.jsonPath().getLong("id");
        지하철역_삭제요청(id);

        // then
        지하철역_목록에_포함되어있지_않다("삼성역");
    }

    private void 지하철역_목록에_포함되어있다(String... name) {
        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_조회요청();
        final List<String> names = 지하철역_목록
                .jsonPath()
                .getList("name", String.class);
        assertThat(names).contains(name);
    }
    private void 지하철역_목록에_포함되어있지_않다(String... name) {
        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_조회요청();
        final List<String> names = 지하철역_목록
                .jsonPath()
                .getList("name", String.class);
        assertThat(names).doesNotContain(name);
    }

    public static ExtractableResponse<Response> 지하철역_생성요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

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