package subway.section;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.SectionRequest;

public class SectionApiClient {
    public ExtractableResponse<Response> createSection(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
            .body(sectionRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections", lineId)
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
            .pathParam("lineId", lineId)
            .queryParam("stationId", stationId)
            .when().delete("/lines/{lineId}/sections")
            .then().log().all()
            .extract();
    }
}
