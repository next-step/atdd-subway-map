package subway.domain.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

import static subway.common.AcceptanceTest.*;

public class LineApiTest {
    public static ExtractableResponse<Response> 지하철노선을_생성한다(Map<String, Object> param) {
        return POST("/lines", param);
    }

    public static ExtractableResponse<Response> 지하철노선에_구간을_추가한다(int id, Map<String, Object> param) {
        return POST("/lines/{id}/sections", id, param);
    }

    public static ExtractableResponse<Response> 지하철노선을_조회한다() {
        return GET("/lines");
    }

    public static ExtractableResponse<Response> 특정지하철노선을_조회한다(int id) {
        return GET("/lines/{id}", "id", String.valueOf(id));
    }

    public static ExtractableResponse<Response> 지하철노선을_수정한다(int id, Map<String, String> param) {
        return PUT("/lines/{id}", id, param);
    }

    public static ExtractableResponse<Response> 지하철노선을_삭제한다(int id) {
        return DELETE("/lines/{id}", id);
    }

}
