package subway.acceptance.extractableResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.presentation.request.AddSectionRequest;

public class SectionApiExtractableResponse {

    public static ExtractableResponse<Response> addSection(AddSectionRequest addSectionRequest, Long lineId) {
        return RestAssured
                .given().log().all()
                .body(addSectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lindId", lineId)
                .when().post("/lines/{lindId}/section")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", lineId)
                .queryParam("stationId", stationId)
                .when().delete("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }


}
