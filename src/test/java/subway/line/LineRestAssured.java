package subway.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class LineRestAssured {

    public static final String LINE_BASE_PATH = "/lines";
    public static final String LINE_BY_ID_FORMAT = "/lines/{id}";

    public static ExtractableResponse<Response> 노선_생성(
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
                .post(LINE_BASE_PATH)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회(final Long id) {
        return RestAssured.given().log().all()
                .when()
                .get(LINE_BY_ID_FORMAT, id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 노선_목록_조회() {
        return RestAssured
                .given().log().all()
                .when()
                .get(LINE_BASE_PATH)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 노선_수정(
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

    public static ExtractableResponse<Response> 노선_삭제(final String location) {
        return RestAssured.given().log().all()
                .when()
                .delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> Location_조회(final String location) {
        return RestAssured.given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
