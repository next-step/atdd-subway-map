package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineSaveRequest;

import java.util.HashMap;
import java.util.Map;

public class LineApiHelper {

    private LineApiHelper() {
    }

    public static ExtractableResponse<Response> callApiToGetLines() {
        return RestAssured.given()
                          .log()
                          .all()
                          .when()
                          .get("/lines")
                          .then()
                          .log()
                          .all()
                          .extract();
    }

    public static ExtractableResponse<Response> callApiToGetSingleLine(Long lineId) {
        return RestAssured.given()
                          .log()
                          .all()
                          .pathParam("lineId", lineId)
                          .when()
                          .get("/lines/{lineId}")
                          .then()
                          .log()
                          .all()
                          .extract();
    }

    public static ExtractableResponse<Response> callApiToCreateLine(LineSaveRequest lineSaveRequest) {
        return RestAssured.given()
                          .log()
                          .all()
                          .body(lineSaveRequest)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines")
                          .then()
                          .log()
                          .all()
                          .extract();
    }

    public static ExtractableResponse<Response> callApiToModifyLine(Long lineId, LineModifyRequest lineModifyRequest) {
        return RestAssured.given()
                          .log()
                          .all()
                          .pathParam("lineId", lineId)
                          .body(lineModifyRequest)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .put("/lines/{lineId}")
                          .then()
                          .log()
                          .all()
                          .extract();
    }

    public static ExtractableResponse<Response> callApiToDeleteLine(Long lineId) {
        return RestAssured.given()
                          .log()
                          .all()
                          .pathParam("lineId", lineId)
                          .when()
                          .delete("/lines/{lineId}")
                          .then()
                          .log()
                          .all()
                          .extract();
    }
}
