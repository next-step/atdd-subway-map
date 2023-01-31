package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.common.ApiPath;

import java.util.Map;

public class RestAssuredClient {

    public static ExtractableResponse<Response> createStation(Map<String, Object> requestParam) {
        return RestAssuredClient.post(ApiPath.STATION_CREATE_PATH, requestParam);
    }

    public static void deleteStation(Long id) {
        RestAssuredClient.delete(String.format(ApiPath.STATION_DELETE_PATH, id));
    }

    public static ExtractableResponse<Response> getStations() {
        return RestAssuredClient.get(ApiPath.STATION_LIST_PATH);
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

    private static void delete(String path) {
        RestAssured.
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
}
