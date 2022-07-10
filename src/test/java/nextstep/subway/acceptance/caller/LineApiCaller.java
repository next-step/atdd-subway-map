package nextstep.subway.acceptance.caller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineApiCaller {

    public void setPort(int port) {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> createLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> getLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> getLineById(long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> modifyLineById(long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> deleteLineById(long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
}
