package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineUpdateRequest;

import static io.restassured.RestAssured.given;

public class LineApiRequester {

    public ExtractableResponse<Response> createLineApiCall(LineCreateRequest request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> findLinesApiCall() {
        return given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> findLineApiCall(Long id) {
        return given().log().all()
                .pathParam("id", id)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> updateLineApiCall(Long id, LineUpdateRequest request) {
        return given().log().all()
                .pathParam("id", id)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> deleteLineApiCall(Long id) {
        return given().log().all()
                .pathParam("id", id)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }
}
