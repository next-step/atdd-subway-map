package subway.testhelper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.LineUpdateRequest;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class LineApiCaller {

    public ExtractableResponse<Response> callApiCreateLines(Map<String, String> params) {
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> callApiFindLines() {
        return given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> callApiFindLine(String location) {
        return given().log().all()
                .when().get(location)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> callApiUpdateLine(LineUpdateRequest request, String location) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> callApiDeleteLine(String location) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .extract();
    }
}
