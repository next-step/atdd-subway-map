package nextstep.subway.line.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionSteps {

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록요청(Long id, SectionRequest request){
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections ", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_구간_제외요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all().
                when().
                delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().
                extract();
    }
}
