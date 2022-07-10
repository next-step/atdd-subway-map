package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선을 생성한다.")
    @Sql(value = "classpath:sql/station/createStations.sql")
    void createLineTest() {
        //when
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "신분당선");
        requestBody.put("color", "bg-red-600");
        requestBody.put("upStationId", 1);
        requestBody.put("downStationId", 2);
        requestBody.put("distance", 10);

        ExtractableResponse<Response> response = createLine(requestBody);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
    }

    private ExtractableResponse<Response> createLine(Map<String, Object> map) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(map)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철 노선목록을 조회한다.")
    @Sql(value = "classpath:sql/station/createStations.sql")
    void getLinesTest() {
        //given
        Map<String, Object> requestBody1 = new HashMap<>();
        requestBody1.put("name", "신분당선");
        requestBody1.put("color", "bg-red-600");
        requestBody1.put("upStationId", 1);
        requestBody1.put("downStationId", 2);
        requestBody1.put("distance", 10);

        Map<String, Object> requestBody2 = new HashMap<>();
        requestBody2.put("name", "2호선");
        requestBody2.put("color", "green");
        requestBody2.put("upStationId", 1);
        requestBody2.put("downStationId", 2);
        requestBody2.put("distance", 10);

        createLine(requestBody1);
        createLine(requestBody2);

        //when
        ExtractableResponse<Response> subwayLines = getLines();

        //then
        assertThat(subwayLines.jsonPath().getList("")).hasSize(2);
    }

    private ExtractableResponse<Response> getLines() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLineTest() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLineTest() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void deleteLineTest() {

    }
}
