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
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineResponse;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StationLineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // when
        Map<String, String> params = getParamMap("신분당선", "bg-red-600", "1", "2");

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

    private static Map<String, String> getParamMap(
        String name,
        String color,
        String upStationId,
        String downStationId
    ) {
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", name);
        params2.put("color", color);
        params2.put("upStationId", upStationId);
        params2.put("downStationId", downStationId);
        params2.put("distance", "10");
        return params2;
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
        Map<String, String> params1 = getParamMap("신분당선", "bg-red-600", "1", "2");

        Map<String, String> params2 = getParamMap("2호선", "bg-green-600", "1", "3");

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
        Map<String, String> params = getParamMap("신분당선", "bg-red-600", "1", "2");

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

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");

        Long id =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract().jsonPath().getLong("id");

        // when
        Map<String, String> updateParams = getParamMap("다른신분당선", "bg-red-600", "1", "2");

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");

        Long id =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

