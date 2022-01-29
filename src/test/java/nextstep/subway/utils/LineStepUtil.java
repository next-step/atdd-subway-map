package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.HttpRequestTestUtil.*;

public class LineStepUtil {

    public static final String 기본주소 = "/lines";
    public static final String 기존노선 = "기존노선";
    public static final String 기존색상 = "기존색상";
    public static final String 새로운노선 = "새로운노선";
    public static final String 새로운색상 = "새로운색상";
    public static final String 수정노선 = "수정 노선";
    public static final String 수정색상 = "수정 색상";
    public static final int 종점간거리 = 2;
    public static Long 상행종점 = 1L;
    public static Long 하행종점 = 2L;

    private LineStepUtil() {
    }

    public static ExtractableResponse<Response> 노선조회(String url) {
        return 겟_요청(url);
    }


    public static ExtractableResponse<Response> 새로운노선생성() {
        return 노선생성(새로운노선, 새로운색상, 상행종점, 하행종점, 종점간거리);
    }

    public static ExtractableResponse<Response> 노선생성(String 노선이름,String 노선색상,Long 상행역,Long 하행역,int 종점간거리) {
        Map<String, Object> param = 노선파라미터생성(노선이름, 노선색상, 상행역, 하행역, 종점간거리);
        return 포스트_요청(기본주소, param);
    }

    public static ExtractableResponse<Response> 노선수정(ExtractableResponse<Response> createResponse) {
        Map<String, Object> updateParam = 노선파라미터생성(수정노선, 수정색상, 상행종점, 하행종점, 종점간거리);
        ExtractableResponse<Response> updateResponse = 풋_요청(createResponse.header(HttpHeaders.LOCATION), updateParam);
        return updateResponse;
    }

    public static Map<String, Object> 노선파라미터생성(String 노선, String 색상, Long 상행종점, Long 하행종점, int 종점간거리) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", 노선);
        param.put("color", 색상);
        param.put("upStationId", 상행종점);
        param.put("downStationId", 하행종점);
        param.put("distance", 종점간거리);
        return param;
    }
}
