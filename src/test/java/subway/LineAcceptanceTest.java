package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lines.LineApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = LineApplication.class)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> line = createLine("신분당선", "bg-red-600", "1", "2", "10");
        assertThat(line.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines/" + line.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        String name = response.jsonPath().getString("name");
        assertThat(name).isEqualTo("신분당선");
    }


    /**
     * Given 2개의 지하철 노선을 생성하고 When 지하철 노선 목록을 조회하면 Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines() {

        // given
        ExtractableResponse<Response> line = createLine("신분당선", "bg-red-600", "1", "2", "10");
        assertThat(line.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> line2 = createLine("분당선", "bg-green-600", "3", "4", "10");
        assertThat(line2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        // then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선", "분당선");
    }


    /**
     * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 조회하면 Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> line = createLine("신분당선", "bg-red-600", "1", "2", "10");
        assertThat(line.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines/" + line.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        // then
        String lineName = response.jsonPath().getString("name");
        assertThat(lineName).contains("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 수정하면 Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {

        // given
        ExtractableResponse<Response> line = createLine("신분당선", "bg-red-600", "1", "2", "10");
        assertThat(line.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        Map<String, String> param2 = new HashMap<>();
        param2.put("name", "다른분당선");
        param2.put("color", "bg-red-600");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(param2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/" + line.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        // then
        String lineName = response.jsonPath().getString("name");
        assertThat(lineName).contains("다른분당선");
    }


    /**
     * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 삭제하면 Then 해당 지하철 노선 정보는 삭제된다.
     */

    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> line = createLine("신분당선", "bg-red-600", "1", "2", "10");
        ;
        assertThat(line.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().delete("/lines/" + line.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }


    private ExtractableResponse<Response> createLine(String name, String color, String upStationId,
        String downStationId, String distance) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);

        return RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> createStation(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        return RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();

    }
}