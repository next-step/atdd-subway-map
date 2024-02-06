package subway.acceptance.extractableResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.presentation.request.AddSectionRequest;

public class SectionApiExtractableResponse {

    public static ExtractableResponse<Response> addSection(Long id, AddSectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .pathParam("id", id)
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

}
