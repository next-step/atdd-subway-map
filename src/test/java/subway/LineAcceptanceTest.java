package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;
import subway.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql("/truncate.sql")
public class LineAcceptanceTest {
    @Autowired
    StationRepository stationRepository;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운지하철역 = stationRepository.save(new Station("새로운지하철역"));

        // when
        Map<String, Object> params = createParams("신분당선", "bg-red-600", 지하철역.getId(), 새로운지하철역.getId(), 10);
        지하철노선을_생성한다(params);

        // then
        ExtractableResponse<Response> response = 지하철노선_목록을_조회한다();
        List<String> lines = response.jsonPath().getList("name", String.class);

        assertThat(lines).containsAnyOf("신분당선");
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void selectLines() {
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운지하철역 = stationRepository.save(new Station("새로운지하철역"));
        Station 또다른지하철역 = stationRepository.save(new Station("또다른지하철역"));

        // given
        Map<String, Object> params_A = createParams("신분당선", "bg-red-600", 지하철역.getId(), 새로운지하철역.getId(), 10);
        Map<String, Object> params_B = createParams("분당선", "bg-green-600", 지하철역.getId(), 또다른지하철역.getId(), 10);

        지하철노선을_생성한다(params_A);
        지하철노선을_생성한다(params_B);

        // when
        ExtractableResponse<Response> response = 지하철노선_목록을_조회한다();
        List<String> lineNames = response.jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames.size()).isEqualTo(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void selectLine() {
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운지하철역 = stationRepository.save(new Station("새로운지하철역"));

        // given
        Map<String, Object> params = createParams("신분당선", "bg-red-600", 지하철역.getId(), 새로운지하철역.getId(), 10);
        ExtractableResponse<Response> response = 지하철노선을_생성한다(params);
        long createId = response.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> extract = 지하철노선_한개를_조회한다(createId);

        // then
        long id = extract.jsonPath().getLong("id");
        assertThat(createId).isEqualTo(id);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운지하철역 = stationRepository.save(new Station("새로운지하철역"));

        // given
        Map<String, Object> params = createParams("신분당선", "bg-red-600", 지하철역.getId(), 새로운지하철역.getId(), 10);
        ExtractableResponse<Response> createResponse = 지하철노선을_생성한다(params);
        long id = createResponse.jsonPath().getLong("id");

        // when
        final String modifyName = "다른분당선";
        final String modifyColor = "bg-red-600";
        지하철노선_한개를_수정한다(id, modifyName, modifyColor);

        // then
        ExtractableResponse<Response> selectResponse = 지하철노선_한개를_조회한다(id);

        assertThat(selectResponse.jsonPath().getString("name")).isEqualTo(modifyName);
        assertThat(selectResponse.jsonPath().getString("color")).isEqualTo(modifyColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운지하철역 = stationRepository.save(new Station("새로운지하철역"));

        // given
        Map<String, Object> params = createParams("신분당선", "bg-red-600", 지하철역.getId(), 새로운지하철역.getId(), 10);
        ExtractableResponse<Response> response = 지하철노선을_생성한다(params);
        long id = response.jsonPath().getLong("id");

        // when, then
        지하철노선을_하나를_삭제한다(id);
    }

    private void 지하철노선_한개를_수정한다(Long id, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철노선을_하나를_삭제한다(Long id) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_생성한다(Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private static ExtractableResponse<Response> 지하철노선_한개를_조회한다(long id) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private Map<String, Object> createParams(String name, String color, Long upStationId, Long downStationId
            , int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }
}
