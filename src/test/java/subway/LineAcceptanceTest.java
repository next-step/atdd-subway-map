package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        //given
        String 신분당선 = "신분당선";
        String color = "bg-red-600";
        long upStationId = 1;
        long downStationId = 2;
        int distance = 10;

        //when
        ExtractableResponse<Response> response = 지하철역_노선_등록_요청(
            신분당선, color, upStationId, downStationId, distance
        );

        //then
        Assertions.assertAll(
            () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(1L),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선),
            () -> assertThat(response.jsonPath().getLong("upStationId")).isEqualTo(upStationId),
            () -> assertThat(response.jsonPath().getLong("downStationId")).isEqualTo(downStationId),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance)
        );

        //Then
        assertThat(지하철역_노선_목록_조회_요청().jsonPath().getList("id"))
            .contains(Long.valueOf(response.jsonPath().getLong("id")).intValue());
    }

    private static ExtractableResponse<Response> 지하철역_노선_등록_요청(String name, String color
        , long upStationId, long downStationId, int distance) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    private static ExtractableResponse<Response> 지하철역_노선_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

}
