package subway.line;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;

public class LineApiClient {
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

}
