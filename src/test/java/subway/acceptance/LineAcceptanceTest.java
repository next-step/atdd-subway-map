package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.StationAcceptanceTest.지하철_역_생성_요청;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * Given 2개의 지하철 역을 생성하고
     * When (해당 지하철 역을 포함하는) 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성 한다.")
    @Test
    void createLine() {
        // given
        Long upStationId = 지하철_역_생성_요청("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성_요청("양재역").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 10L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    private static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();
        return response;
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선을 목록을 조회 한다.")
    @Test
    void getLines() {
        // given
        Long station1 = 지하철_역_생성_요청("강남역").jsonPath().getLong("id");
        Long station2 = 지하철_역_생성_요청("양재역").jsonPath().getLong("id");
        Long station3 = 지하철_역_생성_요청("역삼역").jsonPath().getLong("id");
        Long station4 = 지하철_역_생성_요청("선릉역").jsonPath().getLong("id");

        지하철_노선_생성_요청("신분당선", "bg-red-600", station1, station2, 10L);
        지하철_노선_생성_요청("2호선", "bg-green-600", station3, station4, 10L);

        List<String> lineNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회 한다.")
    @Test
    void getLine() {
        // given
        Long upStationId = 지하철_역_생성_요청("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철_역_생성_요청("양재역").jsonPath().getLong("id");

        Long id = 지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 10L).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", id)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();

        assertThat(response.jsonPath().getLong("id")).isEqualTo(id);
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.jsonPath().getList("stations.id", Long.class))
                .containsAnyOf(upStationId, downStationId);

    }
}
