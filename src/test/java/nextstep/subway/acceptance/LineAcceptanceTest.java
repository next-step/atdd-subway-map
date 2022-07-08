package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("지하철 노선 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private String upStationId;
    private String downStationId;

    @BeforeEach
    void stationSetUp() {
        final ExtractableResponse<Response> 강남역 = StationAcceptanceTest.지하철역_생성요청(Maps.of("name", "강남역"));
        final ExtractableResponse<Response> 역삼역 = StationAcceptanceTest.지하철역_생성요청(Maps.of("name", "역삼역"));
        final ExtractableResponse<Response> 선릉역 = StationAcceptanceTest.지하철역_생성요청(Maps.of("name", "선릉역"));
        final ExtractableResponse<Response> 삼성역 = StationAcceptanceTest.지하철역_생성요청(Maps.of("name", "삼성역"));

        upStationId = 강남역.jsonPath().getString("id");
        downStationId = 선릉역.jsonPath().getString("id");
    }

    @AfterEach
    void cleanUp() {
        final ExtractableResponse<Response> 지하철노선_목록_응답 = 지하철노선_목록_조회요청();
        final List<Long> ids = 지하철노선_목록_응답.jsonPath().getList("id", Long.class);
        ids.forEach(this::지하철노선_삭제요청);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    @Order(1)
    void createLine() {
        // when
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "분당선");
        params1.put("color", "bg-green-600");
        params1.put("upStationId", upStationId);
        params1.put("downStationId", downStationId);
        params1.put("distance", "5");
        지하철노선_생성요청(params1);

        // then
        final ExtractableResponse<Response> response = 지하철노선_목록_조회요청();
        final List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames).contains("분당선");

        final List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).contains("[강남역, 선릉역]");
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
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "분당선");
        params1.put("color", "bg-green-600");
        params1.put("upStationId", "1");
        params1.put("downStationId", "3");
        params1.put("distance", "5");
        지하철노선_생성요청(params1);

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신분당선");
        params2.put("color", "bg-red-600");
        params2.put("upStationId", "1");
        params2.put("downStationId", "2");
        params2.put("distance", "10");
        지하철노선_생성요청(params2);

        // when
        final ExtractableResponse<Response> response = 지하철노선_목록_조회요청();

        // then
        final List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames.size()).isEqualTo(2);
        assertThat(lineNames).contains("분당선", "신분당선");
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
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "분당선");
        params1.put("color", "bg-green-600");
        params1.put("upStationId", "1");
        params1.put("downStationId", "3");
        params1.put("distance", "5");
        final ExtractableResponse<Response> registryResponse = 지하철노선_생성요청(params1);

        // when
        final Long id = registryResponse.jsonPath().getLong("id");

        //then
        final ExtractableResponse<Response> getLineResponse = 지하철노선_조회요청(id);
        final JsonPath jsonPath = getLineResponse.jsonPath();

        final String name = jsonPath.getString("name");
        final String color = jsonPath.getString("color");

        assertThat(name).isEqualTo(params1.get("name"));
        assertThat(color).isEqualTo(params1.get("color"));
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
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "분당선");
        params1.put("color", "bg-green-600");
        params1.put("upStationId", "1");
        params1.put("downStationId", "3");
        params1.put("distance", "5");
        final ExtractableResponse<Response> registryResponse = 지하철노선_생성요청(params1);

        // when
        final Long id = registryResponse.jsonPath().getLong("id");

        //then
        params1.put("name", "뉴분당선");
        params1.put("upStationId", "2");
        params1.put("downStationId", "4");
        final ExtractableResponse<Response> updateLineResponse = 지하철노선_수정요청(id, params1);
        final JsonPath jsonPath = updateLineResponse.jsonPath();

        final String name = jsonPath.getString("name");
        final String color = jsonPath.getString("color");

        assertThat(name).isEqualTo(params1.get("name"));
        assertThat(color).isEqualTo(params1.get("color"));
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
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "분당선");
        params1.put("color", "bg-green-600");
        params1.put("upStationId", "1");
        params1.put("downStationId", "3");
        params1.put("distance", "5");
        final ExtractableResponse<Response> registryResponse = 지하철노선_생성요청(params1);

        // when
        final long id = registryResponse.jsonPath().getLong("id");
        지하철노선_삭제요청(id);

        // then
        final ExtractableResponse<Response> getResponse = 지하철노선_조회요청(id);
        assertThat(getResponse.response().getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 지하철노선_생성요청(Map<String, String> params) {
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

    private ExtractableResponse<Response> 지하철노선_조회요청(Long id) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_수정요청(Long id, Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_삭제요청(long id) {
        return RestAssured.given().log().all()
                .when().delete("lines/" + id)
                .then().log().all()
                .extract();
    }
}
