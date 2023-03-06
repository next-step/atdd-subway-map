package subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.SectionRequest;


public class SectionSteps {

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(Long downStationId, Long upStationId, int distance, Long id) {
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, distance);

        return RestAssured.given().log().all()
                .body(sectionRequest).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .when().post("/lines/{id}/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .queryParam("stationId", stationId)
                .when().delete("/lines/{id}/sections")
                .then().log().all().extract();
    }




}
