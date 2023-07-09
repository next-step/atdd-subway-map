package subway.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

import static subway.util.AcceptanceTestUtil.*;

public class StationSteps {

    public static StationResponse 지하철역_생성_요청_Response_반환(String name) {
        return post("/stations", new StationRequest(name)).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        return post("/stations", new StationRequest(name));
    }

    public static List<String> 지하철역_목록_조회_요청() {
        return get("/stations", "name", String.class);
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(Long stationId) {
        return delete("/stations/{id}", stationId);
    }
}
