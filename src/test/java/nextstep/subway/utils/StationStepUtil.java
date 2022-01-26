package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.HttpRequestTestUtil.겟_요청;
import static nextstep.subway.utils.HttpRequestTestUtil.포스트_요청;

public class StationStepUtil {

    public static final String 기본주소 = "/stations";
    public static final String 기존지하철 = "기존지하철";
    public static final String 새로운지하철 = "새로운지하철";

    public static void 테스트준비_지하철역들생성() {
        기존지하철역생성();
        새로운지하철역생성(새로운지하철);
    }

    public static ExtractableResponse<Response> 기존지하철역생성() {
        Map<String, Object> params = 지하철역파라미터생성(기존지하철);
        return 포스트_요청(기본주소, params);
    }

    public static ExtractableResponse<Response> 새로운지하철역생성(String 지하철역이름) {
        Map<String, Object> params = 지하철역파라미터생성(지하철역이름);
        return 포스트_요청(기본주소, params);
    }

    public static ExtractableResponse<Response> 지하철역조회() {
        return  겟_요청(기본주소);
    }

    private static Map<String, Object> 지하철역파라미터생성(String 지하철역) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", 지하철역);
        return params;
    }

}
