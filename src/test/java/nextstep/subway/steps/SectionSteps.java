package nextstep.subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionSteps {
    public static ExtractableResponse<Response> executeSectionCreateRequest(Long lineId, SectionRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections", lineId)
                .then().log().body()
                .extract();
    }

}
