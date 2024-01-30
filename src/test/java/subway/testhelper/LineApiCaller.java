package subway.testhelper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineUpdateRequest;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class LineApiCaller {

    public static ExtractableResponse<Response> callApiCreateLines(Map<String, String> params) {
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> callApiFindLines() {
        return given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> callApiFindLine(String location) {
        return given().log().all()
                .when().get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> callApiUpdateLine(LineUpdateRequest request,
                                                                  String location) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> callApiDeleteLine(String location) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> callApiUpdateSections(Map<String, String> params,
                                                                      String location) {
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(location + "/sections")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> callApiDeleteSection(String location,
                                                                     String id) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location + "{id}" + "/sections", id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
