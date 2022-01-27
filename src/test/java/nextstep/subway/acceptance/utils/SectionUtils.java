package nextstep.subway.acceptance.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionUtils {

    private SectionUtils() {}

    public static ExtractableResponse<Response> 지하철노선_구간생성요청(Long lineId, SectionRequest request) {



        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(request.getUpStationId()));
        params.put("downStationId", String.valueOf(request.getDownStationId()));
        params.put("distance", String.valueOf(request.getDistance()));

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().extract();
    }
}
