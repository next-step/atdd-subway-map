package nextstep.subway.acceptance.client;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class LineClient {

    private static final String STATIONS_PATH = "/stations";
    private static final String STATION_PATH = "/stations/{id}";

    private final ApiCRUD apiCRUD;

    public LineClient(ApiCRUD apiCRUD) {
        this.apiCRUD = apiCRUD;
    }

    public ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        HashMap<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("name", name);
        jsonBody.put("color", color);
        jsonBody.put("upStationId", upStationId);
        jsonBody.put("downStationId", downStationId);
        jsonBody.put("distance", distance);
        return apiCRUD.create("/lines", jsonBody);
    }

    public ExtractableResponse<Response> fetchLines() {
        return apiCRUD.read("/lines");
    }

    public ExtractableResponse<Response> fetchLine(Long lineId) {
        return apiCRUD.read("/lines/{id}", lineId);
    }

    public ExtractableResponse<Response> modifyLine(Long lineId, String name, String color) {
        HashMap<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("name", name);
        jsonBody.put("color", color);
        return apiCRUD.update("/lines/" + lineId, jsonBody);
    }

    public ExtractableResponse<Response> deleteLine(Long lineId) {
        return apiCRUD.delete("/lines/{id}", lineId);
    }

}
