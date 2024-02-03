package subway.helper.api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.LineCreateRequestBody;
import subway.controller.dto.SectionCreateRequestBody;

public class LineApi {
    private static final String routePrefix = "/lines";

    public static JsonPath createLine(LineCreateRequestBody requestBody) {
        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(routePrefix)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath();
    }

    public static ExtractableResponse<Response> createSection(Long lineId, SectionCreateRequestBody requestBody) {
        return RestAssured.given().log().all()
                .body(requestBody)
                .pathParam("id", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(routePrefix + "/{id}/sections")
                .then().log().all().extract();
    }

    public static JsonPath getLines() {
        return RestAssured.given().log().all()
                .when().get(routePrefix)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    public static JsonPath getLine(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when().get(routePrefix + "/{id}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    public static JsonPath getSection(Long lineId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .when().get(routePrefix + "/{id}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    public static ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", stationId)
                .pathParam("id", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(routePrefix + "/{id}/sections")
                .then().log().all().extract();
    }
}
