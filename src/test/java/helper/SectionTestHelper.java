package helper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class SectionTestHelper {
    private SectionTestHelper() {
    }

    public static List<Long> 지하철구간목록을_조회한다(String lineId) {
        return RequestApiHelper.get("/lines/" + lineId + "/sections")
                .jsonPath()
                .getList("id", Long.class);
    }

    public static ExtractableResponse<Response> 지하철구간을_생성한다(String lineId, Map<String, Object> params) {
        return RequestApiHelper.post("/lines/" + lineId + "/sections", params);
    }

    public static ExtractableResponse<Response> 지하철구간을_삭제한다(String lineId, String stationId) {
        return RequestApiHelper.delete("/lines/" + lineId + "/sections?stationId=" + stationId);
    }
}
