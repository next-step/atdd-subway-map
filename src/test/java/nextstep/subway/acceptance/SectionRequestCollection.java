package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionDeleteRequest;
import org.springframework.http.MediaType;

import java.util.HashMap;

public class SectionRequestCollection {

    public static int 지하철_구간_삭제(SectionDeleteRequest request) {
        return RestAssured
                .given().log().all()
                .queryParam("stationId", request.getDownStationId())
                .when().delete("/lines/{lineId}/sections", request.getLineId())
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

    public static int 지하철_구간_생성_요청(ExtractableResponse<Response> line, ExtractableResponse<Response> station) {
        long lineId = line.jsonPath().getLong("id");
        long downStationId = line.jsonPath().getLong("stations[1].id");
        long newStationId = station.jsonPath().getLong("id");
        return 지하철_구간_등록(lineId, downStationId, newStationId, 2);
    }
}
