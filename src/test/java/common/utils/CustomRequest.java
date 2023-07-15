package common.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class CustomRequest {
    public static Response requestGet(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract().response();
    }

    public static Response requestGet(String path, Map<String, ?> pathParams) {
        return RestAssured.given().log().all()
                .when().get(path, pathParams)
                .then().log().all()
                .extract().response();
    }

    public static Response requestPost(String path, Map<String, ?> params) {
        return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all().extract().response();
    }

    public static Response requestPost(String path, Map<String, ?> pathParams, Map<String, ?> params) {
        return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path, pathParams)
                .then().log().all().extract().response();
    }

    public static Response requestDelete(String path) {
        return RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all().extract().response();
    }

    public static Response requestDelete(String path, Map<String, ?> pathParams) {
        return RestAssured.given().log().all()
                .when().delete(path, pathParams)
                .then().log().all().extract().response();
    }
}
