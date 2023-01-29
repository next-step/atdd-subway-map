package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면 <br>
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선 생성")
    @Test
    void 노선_생성() {
        // when
        final String 신분당선 = "신분당선";
        노선을_생성한다(신분당선, "bg-red-600", 1, 2, 10);

        // then
        final ExtractableResponse<Response> response = 노선_목록을_조회한다();
        final List<String> lineNames = response.body().jsonPath().getList("name", String.class);

        assertThat(lineNames).isNotEmpty()
            .contains(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고 <br>
     * When 지하철 노선 목록을 조회하면 <br>
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("노선 목록 조회")
    @Test
    void 노선_목록_조회() {
        // given
        final String 신분당선 = "신분당선";
        final String 이호선 = "2호선";
        노선을_생성한다(신분당선, "bg-red-600", 1, 2, 10);
        노선을_생성한다(이호선, "green", 3, 4, 20);

        // when
        final ExtractableResponse<Response> lineResponse = 노선_목록을_조회한다();
        final List<String> lineNames = lineResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames).hasSize(2)
            .contains(신분당선, 이호선);
    }

    private ExtractableResponse<Response> 노선을_생성한다(
        String name,
        String color,
        long upStationId,
        long downStationId,
        int distance
    ) {
        final Map<String, Object> params = Map.of(
            "name", name,
            "color", color,
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance
        );
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", containsString("lines"))
            .extract();
    }

    private ExtractableResponse<Response> 노선_목록을_조회한다() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

}
