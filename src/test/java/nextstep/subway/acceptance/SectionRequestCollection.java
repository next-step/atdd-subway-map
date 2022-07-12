package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.HashMap;

public class SectionRequestCollection {

    public static int 지하철_구간_삭제(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .queryParam("stationId", stationId)
                .when().delete("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract()
                .statusCode();
    }

    public static int 지하철_구간_등록(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("upStationId", upStationId.toString());
        requestParam.put("downStationId", downStationId.toString());
        requestParam.put("distance", distance.toString());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract()
                .statusCode();
    }
}
