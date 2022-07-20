package nextstep.subway.acceptance.client;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestAssuredCRUD implements ApiCRUD {

    @Override
    public <T> ExtractableResponse<Response> create(String path, T jsonBody) {
        return RestAssured.given().log().all()
                .body(jsonBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    @Override
    public ExtractableResponse<Response> read(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    @Override
    public <T> ExtractableResponse<Response> read(String path, T pathVariable) {
        return RestAssured.given().log().all()
                .when().get(path, pathVariable)
                .then().log().all()
                .extract();
    }

    @Override
    public <T> ExtractableResponse<Response> update(String path, T jsonBody) {
        return RestAssured.given().log().all()
                .body(jsonBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    @Override
    public <T> ExtractableResponse<Response> delete(String path, T pathVariable) {
        return RestAssured.given().log().all()
                .body(pathVariable)
                .when().delete(path, pathVariable)
                .then().log().all()
                .extract();
    }

}
