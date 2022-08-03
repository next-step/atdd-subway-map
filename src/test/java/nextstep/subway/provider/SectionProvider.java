package nextstep.subway.provider;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SectionProvider extends Provider {

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        final Map<String, Object> 지하철_구간_등록_데이터 = 지하철_구간_생성_데이터_생성(upStationId, downStationId, distance);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(지하철_구간_등록_데이터)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
        return response;
    }

    public static Long 지하철_구간_생성됨(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        final ExtractableResponse<Response> 지하철_구간_생성_응답 = 지하철_구간_생성_요청(lineId, upStationId, downStationId, distance);
        return 지하철_구간_생성_응답.jsonPath().getLong("downStationId");
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();
        return response;
    }

    private static Map<String, Object> 지하철_구간_생성_데이터_생성(Long upStationId, Long downStationId, Integer distance) {
        final Map<String, Object> createSectionRequest = new HashMap<>();
        createSectionRequest.put("upStationId", upStationId);
        createSectionRequest.put("downStationId", downStationId);
        createSectionRequest.put("distance", distance);

        return createSectionRequest;
    }
}
