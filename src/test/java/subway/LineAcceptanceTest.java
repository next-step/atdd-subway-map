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
        final String 신분당선 = "신분당선";
        final Map<String, Object> params = Map.of(
            "name", 신분당선,
            "color", "bg-red-600",
            "upStationId", 1,
            "downStationId", 2,
            "distance", 10
        );
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", containsString("lines"))
            .extract();

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        final List<String> lineNames = response.body().jsonPath().getList("name", String.class);

        assertThat(lineNames).isNotEmpty();
    }

}
