package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.CreatedLineResponse;
import nextstep.subway.station.StationSteps;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {

    public static ExtractableResponse< Response > registerLineWithStationsHelper(
            final Map<String, String> newLine,
            final Map<String, String> upStation,
            final Map<String, String> downStation,
            final int distance) {
        StationResponse upStationResponse = StationSteps.createStationHelper(upStation).as(StationResponse.class);
        StationResponse downStationResponse = StationSteps.createStationHelper(downStation).as(StationResponse.class);
        newLine.put("upStationId", upStationResponse.getId().toString());
        newLine.put("downStationId", downStationResponse.getId().toString());
        newLine.put("distance", String.valueOf(distance));
        return registerLineHelper(newLine);
    }

    public static CreatedLineResponse registerLineWithStationsHelper(
            final String lineName,
            final String lineColor,
            final String upStationName,
            final String downStationName,
            final int distance) {
        final Map newLine = createLineMapHelper(lineName,lineColor);
        final Map upStation = StationSteps.createStationInputHelper(upStationName);
        final Map downStation = StationSteps.createStationInputHelper(downStationName);
        return registerLineWithStationsHelper(newLine, upStation, downStation, distance)
                .as(CreatedLineResponse.class);
    }

    public static ExtractableResponse< Response > getLinesHelper() {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    get("/lines").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> registerLineHelper(final Map<String, String> line) {
        return RestAssured.given().log().all().
                    body(line).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    post("/lines").
                then().
                    log().all().
                    extract();
    }

    public static  ExtractableResponse<Response> findLineHelper(final CreatedLineResponse inputLine) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    pathParam("lineId", inputLine.getId()).
                    get("/lines/{lineId}").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> updateLineHelper(final Long lineId, final Map<String, String> line) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(line).
                when().
                    pathParam("lineId", lineId).
                    patch("/lines/{lineId}").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> deleteLineHelper(final Long lineId) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    pathParam("lineId", lineId).
                    delete("/lines/{lineId}").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> registerSectionHelper(final Long lineId, final Map newSection) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    body(newSection).
                    pathParam("lineId", lineId).
                    post("/lines/{lineId}/sections").
                then().
                    log().all().
                    extract();
    }

    public static ExtractableResponse<Response> deleteStationHelper(final Long lineId, final Long stationId) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                    pathParam("lineId", lineId).
                    queryParam("stationId", stationId).
                    delete("/lines/{lineId}/sections").
                then().
                    log().all().
                    extract();
    }

    public static Map createLineAndStationMapHelper(
            final String lineName,
            final String lineColor,
            final Long upStationId,
            final Long downStationId,
            final int distance){
        Map lineMap =  new HashMap<String, String>() {
            {
                put("name", lineName);
                put("color", lineColor);
            }
        };
        lineMap.putAll(createSectionMapHelper(upStationId, downStationId, distance));
        return lineMap;
    }

    public static Map createLineMapHelper(
            final String lineName,
            final String lineColor){
        return new HashMap<String, String>() {
            {
                put("name", lineName);
                put("color", lineColor);
            }
        };
    }

    public static Map createSectionMapHelper(
            final Long upStationId,
            final Long downStationId,
            final int distance
    ){
        return Map.of("upStationId", String.valueOf(upStationId),
                "downStationId", String.valueOf(downStationId),
                "distance", String.valueOf(distance));
    }
}
