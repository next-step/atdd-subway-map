package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/setup-station.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static final Map<String, String> 신분당선;
    private static final Map<String, String> 분당선;

    static {
        신분당선 = new HashMap<>();
        신분당선.put("name", "신분당선");
        신분당선.put("color", "bg-red-600");
        신분당선.put("upStationId", "1");
        신분당선.put("downStationId", "2");
        신분당선.put("distance", "10");

        분당선 = new HashMap<>();
        분당선.put("name", "분당선");
        분당선.put("color", "bg-green-600");
        분당선.put("upStationId", "1");
        분당선.put("downStationId", "3");
        분당선.put("distance", "10");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        postLine(신분당선).statusCode(HttpStatus.CREATED.value());

        var response = get("/lines").extract();
        assertThat(response.jsonPath().getList("name")).contains(신분당선.get("name"));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        postLine(신분당선);
        postLine(분당선);

        var response = get("/lines").extract();

        assertThat(response.jsonPath().getList("name"))
                .hasSize(2)
                .contains("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        var createResponse = postLine(신분당선).extract();

        var response = get(createResponse.header("location")).extract();

        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선.get("name"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("name", "다른 분당선");
        updateRequest.put("color", "bg-red-600");
        var location = postLine(신분당선).extract().header("location");

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateRequest).
        when()
                .put(location).
        then().log().all()
                .statusCode(HttpStatus.OK.value());

        var response = get(location)
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        assertThat(response.jsonPath().getString("name")).isEqualTo(updateRequest.get("name"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        var location = postLine(신분당선).extract().header("location");

        given().log().all().
        when()
                .delete(location).
        then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var response = get("/lines").extract();
        assertThat(response.jsonPath().getList("name")).doesNotContain(신분당선.get("name"));
    }

    private ValidatableResponse postLine(Map<String, String> params) {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params).
                when()
                    .post("/lines").
                then().log().all();
    }

    private ValidatableResponse get(String path) {
        return given().log().all().
                when()
                    .get(path).
                then().log().all();
    }
}
