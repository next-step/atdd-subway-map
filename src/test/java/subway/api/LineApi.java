package subway.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.UpdateLineRequest;

public class LineApi {
    public static ExtractableResponse<Response> createLine(LineRequest request) {
        return RestAssured.given()
                .log()
                .all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> getLines() {
        return RestAssured.given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> getLine(Long lineId) {
        return RestAssured.given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/" + lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(Long lineId, UpdateLineRequest request) {
        return RestAssured.given()
                .log()
                .all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long lineId) {
        return RestAssured.given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/" + lineId)
                .then()
                .log()
                .all()
                .extract();
    }
}
