package subway.extractableResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineApiExtractableResponse {

    public static ExtractableResponse<Response> createLine(Map<String, Object> requestParam) {
        return RestAssured
                .given().log().all()
                .body(requestParam)
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

    public static ExtractableResponse<Response> selectLine(Long id) {
        return RestAssured
                .given().log().all()
                .pathParam("id", id)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> modifyLine(Long id, Map<String, Object> requestParam) {
        return RestAssured
                .given().log().all()
                .body(requestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .when().put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }

}
