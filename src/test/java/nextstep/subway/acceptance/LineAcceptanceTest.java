package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("지하철 노선 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private String upStationId;
    private String downStationId;

    @BeforeEach
    void stationSetUp() {
        var 강남역 = StationAcceptanceTest.지하철역_생성요청("강남역");
        var 역삼역 = StationAcceptanceTest.지하철역_생성요청("역삼역");
        var 선릉역 = StationAcceptanceTest.지하철역_생성요청("선릉역");
        var 삼성역 = StationAcceptanceTest.지하철역_생성요청("삼성역");

        upStationId = 강남역.jsonPath().getString("id");
        downStationId = 선릉역.jsonPath().getString("id");
    }

    @AfterEach
    void cleanUp() {
        var 지하철노선_목록 = 지하철노선_목록_조회요청();
        final List<Long> ids = 지하철노선_목록.jsonPath().getList("id", Long.class);
        ids.forEach(this::지하철노선_삭제요청);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    @Order(1)
    void 지하철_노선_생성_인수테스트() {
        // when
        var 분당선 = 지하철_노선을_생성한다("분당선", "bg-green-600", 1, 3, 5);

        // then
        지하철_노선이_생성되었다(분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선목록을 조회한다.")
    @Order(2)
    @Test
    void findLines() {
        // given
        지하철_노선을_생성한다("분당선", "bg-green-600", 1, 3, 5);
        지하철_노선을_생성한다("신분당선", "bg-red-600", 1, 2, 10);

        // when
        var 지하철노선_목록 = 지하철노선_목록_조회요청();

        // then
        지하철_노선이_포함되어_있다(지하철노선_목록, "분당선", "신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Order(3)
    @Test
    void findLine() {
        // given
        var 분당선 = 지하철_노선을_생성한다("분당선", "bg-green-600", 1, 3, 5);

        // when
        var 조회된_지하철_노선 = 지하철노선_조회요청(분당선);

        //then
        지하철_노선이름_일치하는지_확인한다(조회된_지하철_노선, "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Order(4)
    @Test
    void 지하철노선_수정요청() {
        // given
        var 분당선 = 지하철_노선을_생성한다("분당선", "bg-green-600", 1, 3, 5);

        // when
        지하철노선_수정요청(분당선, "뉴분당선", null, 2, 4, null);

        // then
        지하철_노선이름_일치하는지_확인한다(분당선, "뉴분당선");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Order(5)
    @Test
    void deleteLine() {
        // given
        var 분당선 = 지하철_노선을_생성한다("분당선", "bg-green-600", 1, 3, 5);

        // when
        var 삭제응답 = 지하철노선_삭제요청(분당선);

        // then
        assertThat(삭제응답.response().statusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철_노선을_생성한다(String name, String color, int upStationId, int downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_목록_조회요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_조회요청(ExtractableResponse<Response> response) {
        final Long id = response.jsonPath().getLong("id");

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_수정요청(ExtractableResponse<Response> response,
                                                     String name, String color, Integer upStationId,
                                                     Integer downStationId, Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", Optional.ofNullable(upStationId).map(Object::toString).orElse(null));
        params.put("downStationId", Optional.ofNullable(downStationId).map(Object::toString).orElse(null));
        params.put("distance", Optional.ofNullable(distance).map(Object::toString).orElse(null));

        final Long id = response.jsonPath().getLong("id");
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_삭제요청(ExtractableResponse<Response> response) {
        final long id = response.jsonPath().getLong("id");

        return RestAssured.given().log().all()
                .when().delete("lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_삭제요청(long id) {
        return RestAssured.given().log().all()
                .when().delete("lines/" + id)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선이_생성되었다(ExtractableResponse<Response> 분당선) {
        assertThat(분당선.response().statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    private void 지하철_노선이_포함되어_있다(ExtractableResponse<Response> response, String... line) {
        final List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames.size()).isEqualTo(2);
        assertThat(lineNames).contains(line);
    }

    private void 지하철_노선이름_일치하는지_확인한다(ExtractableResponse<Response> response, String name) {
        var 지하철노선 = 지하철노선_조회요청(response);
        final String 지하철_노선명 = 지하철노선.jsonPath().getString("name");
        assertThat(지하철_노선명).isEqualTo(name);
    }
}
