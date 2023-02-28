package subway.feature;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Map;

public class RestAssuredFeature {
    protected static ExtractableResponse<Response> callCreate(final String url, final Object body){
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> callGet(final String url, final Map pathParams){
        return RestAssured.given().log().all()
                .when()
                .get(url, pathParams)
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> callModify(final String url, final Map pathParams){
        return RestAssured.given().log().all()
                .when()
                .body(pathParams)
                .put(url)
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> callGet(final String url){
        return callGet(url, Collections.EMPTY_MAP);
    }

    protected static ExtractableResponse<Response> callDelete(final String url){
        return RestAssured.given().log().all()
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }
}
