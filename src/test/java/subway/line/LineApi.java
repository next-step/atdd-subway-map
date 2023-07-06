package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineApi {

    public static ExtractableResponse<Response> createLine(final Map<String, String> line) {
        return RestAssured.given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> retrieveLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> retrieveLineByLocation(final String createdLocation) {
        return RestAssured.given().log().all()
                .when().get(createdLocation)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> modifyLineByLocation(final String createdLocation, final Map<String, String> request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(createdLocation)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> appendSectionInLine(final String appendLocation, final Map<String, String> request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(appendLocation)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> removeSectionInLine(final Long deleteLocation) {
        return RestAssured.given().log().all()
                .when().delete(String.valueOf(deleteLocation))
                .then().log().all()
                .extract();
    }
}
