package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.presentation.SectionRequest;

public class SectionSteps {
    static ExtractableResponse<Response> createSection(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    static void deleteSection(Long lineId, Long stationId) {
        RestAssured.given().log().all()
                .when().delete("/lines/" + lineId + "/sections/" + stationId)
                .then().log().all();
    }
}
