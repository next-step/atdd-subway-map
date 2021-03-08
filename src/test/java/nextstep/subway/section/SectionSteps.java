package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.section.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionSteps {

    private SectionSteps() {}

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(SectionRequest body, long lineId) {
        return RestAssured
            .given().log().all()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{lineId}/sections", lineId)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_제거_요청(long lineId, long stationId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
            .then().log().all().extract();
    }


}
