package subway;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // when
        HashMap<String, String> API_요청_정보 = new HashMap<>();
        API_요청_정보.put("name", "신분당선");
        API_요청_정보.put("color", "bg-red-600");
        API_요청_정보.put("upStationId", "1");
        API_요청_정보.put("downStationId", "2");
        API_요청_정보.put("distance", "10");

        ExtractableResponse<Response> createdRes = 지하철_노선_생성(API_요청_정보);

        // then
        ExtractableResponse<Response> getLineListRes = 지하철_노선_목록_조회();

        지하철_노선_생성_여부_검증(createdRes, getLineListRes);
    }

    private static void 지하철_노선_생성_여부_검증(ExtractableResponse<Response> createdRes, ExtractableResponse<Response> getLineListRes) {
        String lineName = createdRes.jsonPath().getString("name");
        List<String> lineList = getLineListRes.jsonPath().getList("name", String.class);

        assertThat(lineList).contains(lineName);
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .accept(ContentType.JSON)
                .get("/lines")
                .then().log().all()
                .extract();
        return response;
    }

    private static ExtractableResponse<Response> 지하철_노선_생성(Map<String, String> requestData) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestData)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
        return response;
    }
}
