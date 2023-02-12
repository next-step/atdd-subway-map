package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTest.createStation;

@AcceptanceTest
@DisplayName("노선 관련 기능")
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void test_createStation() {
        // given
        long station1Id = createStation("station1").jsonPath()
                .getLong("id");
        long station2Id = createStation("station2").jsonPath()
                .getLong("id");

        // when
        String newlineName = "newLine";
        Long newLineId = createLine(newlineName, station1Id, station2Id)
                .jsonPath().getLong("id");

        // then
        String responseNewLineName = getLine(newLineId).jsonPath()
                .getString("name");
        assertThat(responseNewLineName).isEqualTo(newlineName);
    }

    public static ExtractableResponse<Response> createLine(String name, Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();
        return response;
    }

    public static ExtractableResponse<Response> getLine(Long id) {
        return RestAssured.given()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
}
