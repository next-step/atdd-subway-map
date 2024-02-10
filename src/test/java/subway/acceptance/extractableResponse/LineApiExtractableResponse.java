package subway.acceptance.extractableResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.presentation.request.AddSectionRequest;
import subway.line.presentation.request.CreateLineRequest;
import subway.line.presentation.request.UpdateLineRequest;

public class LineApiExtractableResponse {

    public static ExtractableResponse<Response> createLine(CreateLineRequest createLineRequest) {
        return RestAssured
                .given().log().all()
                .body(createLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> selectLines() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> selectLine(Long lineId) {
        return RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(Long lineId, UpdateLineRequest updateLineRequest) {
        return RestAssured
                .given().log().all()
                .body(updateLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", lineId)
                .when().put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", lineId)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }

}
