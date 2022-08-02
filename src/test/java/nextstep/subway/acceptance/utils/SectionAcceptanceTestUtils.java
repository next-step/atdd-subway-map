package nextstep.subway.acceptance.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionAcceptanceTestUtils {

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
}
