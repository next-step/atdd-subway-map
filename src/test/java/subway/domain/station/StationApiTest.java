package subway.domain.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static subway.common.AcceptanceTest.*;

public class StationApiTest {
    public static ExtractableResponse<Response> 지하철역을_조회한다() {
        return GET("/stations");
    }

    public static ExtractableResponse<Response> 지하철역을_생성한다(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        return POST("/stations", param);
    }

    public static ExtractableResponse<Response> 지하철역을_삭제한다(String id) {
        return DELETE("/stations/{id}", id);
    }
}
