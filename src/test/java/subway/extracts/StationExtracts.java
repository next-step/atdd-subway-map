package subway.extracts;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class StationExtracts {
    public static Long 지하철역_생성_응답_ID_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    public static List<String> 지하철역_목록_조회_응답_역_이름_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getList("name", String.class);
    }
}
