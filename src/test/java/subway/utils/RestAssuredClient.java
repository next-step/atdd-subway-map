package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.common.ApiPath;

import java.util.Map;

public class RestAssuredClient {

    // Subway Station
    public static ExtractableResponse<Response> createStation(Map<String, Object> requestParam) {
        return RestAssuredClient.post(ApiPath.STATION_CREATE_PATH, requestParam);
    }

    public static void deleteStation(Long id) {
        RestAssuredClient.delete(String.format(ApiPath.STATION_DELETE_PATH, id));
    }

    public static ExtractableResponse<Response> listStation() {
        return RestAssuredClient.get(ApiPath.STATION_LIST_PATH);
    }

    // Subway Line
    public static ExtractableResponse<Response> createLine(Map<String, Object> requestParam) {
        return RestAssuredClient.post(ApiPath.LINE_CREATE_PATH, requestParam);
    }

    public static ExtractableResponse<Response> findLine(Long id) {
        return RestAssuredClient.get(String.format(ApiPath.LINE_GET_PATH, id));
    }

    public static ExtractableResponse<Response> updateLine(Long id, Map<String, Object> requestParam) {
        return RestAssuredClient.put(String.format(ApiPath.LINE_UPDATE_PATH, id), requestParam);
    }

    public static ExtractableResponse<Response> listLine() {
        return RestAssuredClient.get(ApiPath.LINE_LIST_PATH);
    }
    public static ExtractableResponse<Response> deleteLine(Long id) { return RestAssuredClient.delete(String.format(ApiPath.LINE_DELETE_PATH, id)); }

    private static ExtractableResponse<Response> delete(String path) {
        return RestAssured.
                given().log().all()
                .when().delete(path)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> get(String path) {
        return RestAssured
                .given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> post(String path, Map<String, Object> requestParam) {
        return RestAssured
                .given().log().all()
                .body(requestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> put(String path, Map<String, Object> requestParam) {
        return RestAssured
                .given().log().all()
                .body(requestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }
}
