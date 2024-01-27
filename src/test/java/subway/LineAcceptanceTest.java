package subway;

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

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선을_생성() {
        // given
        String lineName = "신분당선";
        Long upStationId = createStationByStationName("강남역").jsonPath().getLong("id");
        Long downStationId = createStationByStationName("신논현역").jsonPath().getLong("id");
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("name", lineName);
        requestParam.put("color", "bg-red-600");
        requestParam.put("upStationId", upStationId);
        requestParam.put("downStationId", downStationId);
        requestParam.put("distance", 10);

        // when
        createLine(requestParam);

        // then
        List<String> stationNames =
                selectLines().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain(lineName);
    }

    private ExtractableResponse<Response> createStationByStationName(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> createLine(Map<String, Object> requestParam) {

        return RestAssured
                .given().log().all()
                .body(requestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> selectLines() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

}
