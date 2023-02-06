package subway.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationUtils {

    public static String GANG_NAM_STATION = "강남역";
    public static String SIN_SA_STATION = "신사역";
    public static String PAN_GYEO_STATION = "판교역";
    public static String SIN_NONE_HYEON_STATION = "신논현역";

    public static String GURO_STATION = "구로역";
    public static String SINDORIM_STATION = "신도림역";
    public static String GASAN_STATION = "가산역";
    public static String SEOUL_STATION = "서울역";

    public static String SIN_BUN_DANG_LINE_NAME = "신분당선";
    public static String ONE_LINE_NAME = "분당선";

    public static String LINE_RED = "bg-red-600";
    public static String LINE_BLUE = "bg-blue-600";


    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static void createStation(String name) {
        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(getRequestSpecification())
                        .body(Map.entry("name", name))
                        .when().post("/stations")
                        .then()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> selectStations() {
        return RestAssured
                .given().spec(StationUtils.getRequestSpecification())
                .when().get("/stations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> createLine(Map<String, Object> body) {
        return RestAssured
                .given().spec(getRequestSpecification()).body(body)
                .when().post("/lines")
                .then().extract();
    }

    public static ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("color", color);
        body.put("upStationId", upStationId);
        body.put("downStationId", downStationId);
        body.put("distance", distance);
        return RestAssured
                .given().spec(getRequestSpecification()).body(body)
                .when().post("/lines")
                .then().extract();
    }


    public static ExtractableResponse<Response> selectLine(long stationLineId) {
        return RestAssured
                .given().spec(getRequestSpecification())
                .when().get("/lines/" + stationLineId)
                .then().extract();
    }

    public static ExtractableResponse<Response> extendLine(Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> body = new HashMap<>();
        body.put("upStationId", upStationId);
        body.put("downStationId", downStationId);
        body.put("distance", distance);
        return RestAssured
                .given().spec(getRequestSpecification()).body(body).log().all()
                .when().post("/lines/1/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> reduceLine(long stationId) {
        return RestAssured
                .given().spec(getRequestSpecification()).log().all()
                .when().delete("/lines/1/sections?stationId=" + stationId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> reduceLine(long lineId, long stationId) {
        return RestAssured
                .given().spec(getRequestSpecification()).log().all()
                .when().delete("/lines/"+ lineId +"/sections?stationId=" + stationId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> createColor(String name) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        return RestAssured
                .given().spec(getRequestSpecification()).body(body)
                .when().post("/color")
                .then().extract();
    }

}
