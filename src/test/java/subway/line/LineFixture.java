package subway.line;

import io.restassured.RestAssured;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static subway.station.StationFixture.지하철역_생성;

public class LineFixture {

    public static long 지하철_노선_생성_ID(LineRequest request) {
        return 지하철_노선_생성(request).getId();
    }

    public static LineResponse 지하철_노선_생성(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject(".", LineResponse.class);
    }


    public static LineResponse 지하철_노선_조회(Long id) {
        return RestAssured.given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", LineResponse.class);
    }

    public static List<Long> 노선에_상행_하행_지하철역_ID(LineResponse response) {
        List<Long> stationIds = response.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        return stationIds;
    }

    public static void 지하철_노선_삭제(Long id) {
        RestAssured.given()
                .when()
                .delete("/lines/" + id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_수정(Long id, LineRequest request) {
        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    public static List<LineResponse> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", LineResponse.class);
    }
}
