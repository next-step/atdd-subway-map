package subway.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class LineRestAssured {

    public ExtractableResponse<Response> createLine(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        Map body = Map.of(
                "name", name,
                "color", color,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance
        );

        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public ExtractableResponse<Response> showLines() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public ExtractableResponse<Response> editLine(
            final String location,
            final String name,
            final String color,
            final int distance
    ) {
        Map body = Map.of(
                "name", name,
                "color", color,
                "distance", distance
        );

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public ExtractableResponse<Response> deleteLine(final String location) {
        return RestAssured.given().log().all()
                .when()
                .delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public ExtractableResponse<Response> requestGet(final String location) {
        return RestAssured.given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
