package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineResponse;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames =
            RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().body().jsonPath().getList(".", LineResponse.class).stream().map(
                    LineResponse::getName).collect(Collectors.toList());
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getStationLines() {
        // given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "신분당선");
        params1.put("color", "bg-red-600");
        params1.put("upStationId", "1");
        params1.put("downStationId", "2");
        params1.put("distance", "10");

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "2호선");
        params2.put("color", "bg-green-600");
        params2.put("upStationId", "1");
        params2.put("downStationId", "3");
        params2.put("distance", "10");

        RestAssured.given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all();

        RestAssured.given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all();

        // when
        List<String> lineNames =
            RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().body().jsonPath().getList(".", LineResponse.class).stream().map(
                    LineResponse::getName).collect(Collectors.toList());

        // then
        assertThat(lineNames).containsAnyOf("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getStationLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        Long id = response.body().jsonPath().getLong("id");
        // when
        String name =
            RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().body().jsonPath().getString("name");

        // then
        assertThat(name).isEqualTo("신분당선");
    }
}
