package subway;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;

public class SubwayRestApiClient {
    public ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> createLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> findAllLines() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> findLineById(Long lineId) {
        return RestAssured.given().log().all()
            .when().get("/lines/{id}", lineId)
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> updateLine(Long lineId, LineRequest lineRequest) {
        return RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}", lineId)
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> deleteLine(Long lineId) {
        return RestAssured.given().log().all()
            .when().delete("/lines/{id}", lineId)
            .then().log().all()
            .extract();
    }

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
