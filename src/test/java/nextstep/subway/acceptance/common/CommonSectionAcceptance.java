package nextstep.subway.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class CommonSectionAcceptance {
    public static ExtractableResponse<Response> 지하철_구간_추가(ExtractableResponse<Response> response, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8")
                .when().post( response.header("location") + "/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제(String stationId,String location) {
        return RestAssured
                .given().log().all()
                .param("stationId",stationId)
                .when().delete(location)
                .then().log().all().extract();
    }

    public static Map<String, String> 구간_파라미터_생성(String downStationId, String newSectionDownStationId) {
        Map<String, String> params =
                new HashMap<>();
        params.put("distance", "10");
        params.put("downStationId", newSectionDownStationId);
        params.put("upStationId", downStationId);
        return params;
    }




}
