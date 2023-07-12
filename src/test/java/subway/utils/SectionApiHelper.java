package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.controller.dto.section.SectionSaveRequest;

public class SectionApiHelper {

    private SectionApiHelper() {
    }

    public static ExtractableResponse<Response> callApiToGetSingleSection(Long sectionId) {
        return RestAssured.given()
                          .log()
                          .all()
                          .pathParam("sectionId", sectionId)
                          .when()
                          .get("/sections/{sectionId}")
                          .then()
                          .log()
                          .all()
                          .extract();
    }

    public static ExtractableResponse<Response> callApiToCreateSection(Long lineId, SectionSaveRequest sectionSaveRequest) {
        return RestAssured.given()
                          .log()
                          .all()
                          .pathParam("lineId", lineId)
                          .body(sectionSaveRequest)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines/{lineId}/sections")
                          .then()
                          .log()
                          .all()
                          .extract();
    }

    public static ExtractableResponse<Response> callApiToDeleteSection(Long lineId, Long stationId) {
        return RestAssured.given()
                          .log()
                          .all()
                          .pathParam("lineId", lineId)
                          .queryParam("stationId", stationId)
                          .when()
                          .delete("/lines/{lineId}/sections")
                          .then()
                          .log()
                          .all()
                          .extract();
    }
}
