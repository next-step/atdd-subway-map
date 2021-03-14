package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.RestAssuredRequest.postRequest;
import static nextstep.subway.line.LineSteps.지하철_노선_조회_요청;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionSteps {

    public static ExtractableResponse<Response> 구간_생성_요청(Long createdLineId, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = createParams(upStationId, downStationId, distance);
        return postRequest("/lines/"+createdLineId+"/sections", params);
    }

    public static void 지하철_구간_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_구간_등록_시_존재하지_않는_지하철_역_오류(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_구간_등록_시_상행역이_노선의_종점역이_아님_오류(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_구간_등록_시_출발역_누락_오류(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_구간_등록_시_종점역_누락_오류(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_구간_등록_시_distance_누락_오류(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 구간_생성_후_연장된_지하철_노선_조회(Long createdLineId) {
        return 지하철_노선_조회_요청(createdLineId);
    }

    private static Map<String, Object> createParams(Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return params;
    }

    public static ExtractableResponse<Response> 상행선으로_사용될_지하철_역_생성(String stationName) {
        return 지하철_역_생성_요청(stationName);
    }

    public static ExtractableResponse<Response> 하행선으로_사용될_지하철_역_생성(String stationName) {
        return 지하철_역_생성_요청(stationName);
    }
}
