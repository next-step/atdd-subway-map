package subway.extracts;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class LineExtracts {
    public static List<Long> 지하철노선_목록_조회_응답_ID_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getList("id", Long.class);
    }

    public static List<String> 지하철노선_목록_조회_응답_노선_이름_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getList("name");
    }

    public static String 지하철노선_조회_응답_노선_이름_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getString("name");
    }

    public static List<Long> 지하철노선_조회_역_ID_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getList("stations.id", Long.class);
    }

    public static Long 지하철노선_생성_응답_ID_추출(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }
}
