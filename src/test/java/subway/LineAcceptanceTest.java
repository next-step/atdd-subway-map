package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void 지하철_노선을_생성하면_목록_조회_시_생성한_노선을_찾을_수_있다() {
        // When
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", 1);
        params.put("downStationId", 2);
        params.put("distance", 10);

        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all();

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        // Then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

}
