package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AbstractAcceptanceTest {
    private static final Map<String, String> 신분당선_요청 = new HashMap<>();
    private static final Map<String, String> 분당선_요청 = new HashMap<>();
    private static final Map<String, String> 잘못된_노선_요청 = new HashMap<>();
    private static final Map<String, String> 수정_요청 = new HashMap<>();
    private static final String 신분당선 = "신분당선";
    private static final String 분당선 = "분당선";
    private static final String 미존재_노선_위치 = "/lines/100";

    static {
        신분당선_요청.put("name", "신분당선");
        신분당선_요청.put("color", "bg-red-600");
        신분당선_요청.put("upStationId", "1");
        신분당선_요청.put("downStationId", "2");
        신분당선_요청.put("distance", "10");

        분당선_요청.put("name", "분당선");
        분당선_요청.put("color", "bg-green-600");
        분당선_요청.put("upStationId", "1");
        분당선_요청.put("downStationId", "3");
        분당선_요청.put("distance", "10");

        잘못된_노선_요청.put("name", "분당선");
        잘못된_노선_요청.put("color", "bg-green-600");
        잘못된_노선_요청.put("upStationId", "1");
        잘못된_노선_요청.put("downStationId", "100");
        잘못된_노선_요청.put("distance", "10");

        수정_요청.put("name", "다른 분당선");
        수정_요청.put("color", "bg-red-600");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void createLine() {
        노선_생성(신분당선_요청).statusCode(HttpStatus.CREATED.value());

        assertThat(노선_목록_이름_조회()).contains(신분당선);
    }

    /**
     * When 존재하지 않는 역을 포함해 생성 요청을 하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 역을 포함시켜 지하철 노선을 생성하면 생성되지 않는다.")
    @Test
    void createLineException() {
        노선_생성(잘못된_노선_요청).statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void showLines() {
        노선_생성(신분당선_요청);
        노선_생성(분당선_요청);
        
        assertThat(노선_목록_이름_조회())
                .hasSize(2)
                .contains(신분당선, 분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void showLine() {
        var createResponse = 노선_생성(신분당선_요청).extract();

        var response = get(createResponse.header("location")).extract();

        String 조회된_노선_이름 = response.jsonPath().getString("name");
        assertThat(조회된_노선_이름).isEqualTo(신분당선);
    }

    /**
     * When 존재하지 않는 지하철 노선을 조회하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 노선을 조회한다.")
    @Test
    void showLineException() {
        given().log().all().
        when()
                .get(미존재_노선_위치).
        then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void updateLine() {
        var location = 노선_생성(신분당선_요청).extract().header("location");

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(수정_요청).
        when()
                .put(location).
        then().log().all()
                .statusCode(HttpStatus.OK.value());

        var 조회된_노선_이름 = get(location)
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .jsonPath()
                    .getString("name");
        assertThat(조회된_노선_이름).isEqualTo("다른 분당선");
    }

    /**
     * When 존재하지 않는 지하철 노선을 수정하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 노선을 수정하는 경우 수정되지 않는다.")
    @Test
    void updateLineException() {
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(수정_요청).
        when()
            .put(미존재_노선_위치).
        then().log().all()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void deleteLine() {
        var location = 노선_생성(신분당선_요청).extract().header("location");

        given().log().all().
        when()
                .delete(location).
        then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(노선_목록_이름_조회()).doesNotContain(신분당선);
    }

    /**
     * When 존재하지 않는 지하철 노선을 삭제하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 노선을 삭제한다.")
    @Test
    void deleteLineException() {
        given().log().all().
        when()
                .delete(미존재_노선_위치).
        then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private ValidatableResponse 노선_생성(Map<String, String> params) {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params).
                when()
                    .post("/lines").
                then().log().all();
    }

    private List<String> 노선_목록_이름_조회() {
        return get("/lines").extract()
                .jsonPath()
                .getList("name");
    }

    private ValidatableResponse get(String path) {
        return given().log().all().
                when()
                    .get(path).
                then().log().all();
    }
}
