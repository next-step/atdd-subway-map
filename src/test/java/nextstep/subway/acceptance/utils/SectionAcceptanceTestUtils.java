package nextstep.subway.acceptance.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionAcceptanceTestUtils {

    public SectionRequest toSectionRequest(String upStationId, String downStationId, Long distance) {
        return SectionRequest.builder()
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }

    public ExtractableResponse<Response> 지하철_구간_등록(SectionRequest request, Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 지하철_구간_제거(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();
    }
}
