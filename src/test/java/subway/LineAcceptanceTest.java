package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.LineRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private String 신분당선_line = "신분당선";
    private String 수인분당선_line = "수인분당선";
    private Long 강남역_id;
    private Long 광교역_id;
    private Long 성수역_id;
    private Long 도곡역_id;

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        강남역_id = 지하철역_생성("강남역");
        광교역_id = 지하철역_생성("광교역");
        성수역_id = 지하철역_생성("성수역");
        도곡역_id = 지하철역_생성("도곡역");
    }

    @AfterEach
    public void cleanup() {
        lineRepository.deleteAll();
    }


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void saveLine() {
        ExtractableResponse<Response> createResponse = 지하철_신분당선_생성();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> lineNames = 지하철_노선_목록_조회();
        assertThat(lineNames).containsAnyOf(신분당선_line);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllLines() {
        지하철_신분당선_생성();
        지하철_수인분당선_생성();

        List<String> lineNames = 지하철_노선_목록_조회();

        assertThat(lineNames).hasSize(2);
        assertThat(lineNames).containsExactly(신분당선_line, 수인분당선_line);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStation() {
        ExtractableResponse<Response> createResponse = 지하철_신분당선_생성();
        Long line_id = createResponse.jsonPath().getLong("id");

        String lineName = 지하철_노선_조회(line_id);
        assertThat(lineName).isEqualTo(신분당선_line);
    }

    private ExtractableResponse<Response> 지하철_신분당선_생성() {
        return 지하철_노선_생성(신분당선_line, "bg-red-600", 강남역_id, 광교역_id, 10);
    }

    private ExtractableResponse<Response> 지하철_수인분당선_생성() {
        return 지하철_노선_생성(수인분당선_line, "bg-yellow-600", 성수역_id, 도곡역_id, 10);
    }

    private ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

    }

    private List<String> 지하철_노선_목록_조회() {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private String 지하철_노선_조회(Long id) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract().jsonPath().getString("name");
    }

    private Long 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getLong("id");
    }
}
