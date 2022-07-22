package nextstep.subway.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionRequest {
    public static ExtractableResponse<Response> 구간_생성_요청(long registLineId, long upStationId, long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("downStationId", String.valueOf(downStationId));
        params.put("upStationId", String.valueOf(upStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post(String.format("/lines/%s/sections", registLineId))
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(long lineId, long stationId) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().delete(String.format("/lines/%s/sections?stationId=%s", lineId, stationId))
                          .then().log().all()
                          .extract();
    }
}
