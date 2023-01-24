package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {

    @Autowired
    private DatabaseStoreCleanup databaseStoreCleanup;

    @BeforeEach
    void setUp() {
        databaseStoreCleanup.cleanStore();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * When 지하철역 조회하면
     * Then 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final ExtractableResponse<Response> 강남역 = 지하철역_생성_요청("강남역");

        지하철역_생성됨(강남역);

        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_요청();

        지하철역_조회됨(지하철역_목록, "강남역");
    }

    private void 지하철역_조회됨(ExtractableResponse<Response> response, final String station) {

        final JsonPath 지하철역_응답_경로 = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철역_응답_경로.getList("")).hasSize(1),
                () -> assertThat(지하철역_응답_경로.getLong("[0].id")).isEqualTo(1L),
                () -> assertThat(지하철역_응답_경로.getString("[0].name")).isEqualTo(station)
        );
    }

    private void 지하철역_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {

        지하철역_생성_요청("잠실역");
        지하철역_생성_요청("검암역");

        final ExtractableResponse<Response> 지하철역_목록_응답 = 지하철역_목록_요청();

        지하철역_목록_조회됨(지하철역_목록_응답, "잠실역", "검암역");
    }

    private void 지하철역_목록_조회됨(final ExtractableResponse<Response> 지하철역_목록_응답, final String...station) {

        final JsonPath 지하철역_응답_경로 = 지하철역_목록_응답.response().body().jsonPath();

        assertAll(
                () -> assertThat(지하철역_목록_응답.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철역_응답_경로.getList("")).hasSize(2),
                () -> assertThat(지하철역_응답_경로.getLong("[0].id")).isEqualTo(1L),
                () -> assertThat(지하철역_응답_경로.getString("[0].name")).isEqualTo(station[0]),
                () -> assertThat(지하철역_응답_경로.getLong("[1].id")).isEqualTo(2L),
                () -> assertThat(지하철역_응답_경로.getString("[1].name")).isEqualTo(station[1])
        );
    }

    private ExtractableResponse<Response> 지하철역_목록_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철역_생성_요청(final String name) {

        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하고
     * When 지하철역 목록 조회를 하면
     * Then 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {

        final ExtractableResponse<Response> 잠실역 = 지하철역_생성_요청("잠실역");

        지하철역_삭제_요청(잠실역);

        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_요청();

        지하철역_조회되지_않음(지하철역_목록);
    }

    private void 지하철역_조회되지_않음(final ExtractableResponse<Response> response) {

        final JsonPath 지하철역_응답_경로 = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철역_응답_경로.getList("")).isEmpty()

        );
    }

    private static ExtractableResponse<Response> 지하철역_삭제_요청(ExtractableResponse<Response> response) {
        final String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private static void 지하철역_삭제됨(final ExtractableResponse<Response> response) {

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}