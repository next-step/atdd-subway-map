package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
        Long id = 지하철_노선을_생성한다("신분당선", "bg-red-600", "1", "2").jsonPath().getLong("id");
        assertThat(id).isNotNull();

        // then
        List<String> lineNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().body().jsonPath().getList(".", LineResponse.class).stream().map(
                                LineResponse::getName).collect(Collectors.toList());
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    private static Map<String, String> getCreateLineParams(
            String name,
            String color,
            String upStationId,
            String downStationId
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");
        return params;
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
        지하철_노선을_생성한다("신분당선", "bg-red-600", "1", "2");
        지하철_노선을_생성한다("2호선", "bg-green-600", "3", "4");

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
        Long id = 지하철_노선을_생성한다("신분당선", "bg-red-600", "1", "2").jsonPath().getLong("id");
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
        Long id = 지하철_노선을_생성한다("신분당선", "bg-red-600", "1", "2").jsonPath().getLong("id");

        // when
        Map<String, String> updateParams = getCreateLineParams("다른신분당선", "bg-red-600", "1", "2");

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
        Long id = 지하철_노선을_생성한다("신분당선", "bg-red-600", "1", "2").jsonPath().getLong("id");
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().delete("/lines/" + id)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 지하철_노선을_생성한다(String name, String color, String upStationId, String downStationId) {
        return RequestFixtures.지하철역_생성_요청하기(getCreateLineParams(name, color, upStationId, downStationId));
    }
}

