package nextstep.subway.acceptance.client;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.aop.ValidateCreated;
import nextstep.subway.acceptance.aop.ValidateNoContent;
import nextstep.subway.acceptance.aop.ValidateOk;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestAssuredCRUD implements ApiCRUD {


    @Override
    @ValidateCreated
    public ExtractableResponse<Response> create(String path, Map<String, Object> jsonBody) {
        return RestAssured.given().log().all()
                .body(jsonBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    @Override
    @ValidateOk
    public ExtractableResponse<Response> read(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    @Override
    @ValidateOk
    public <T> ExtractableResponse<Response> read(String path, T pathVariable) {
        return RestAssured.given().log().all()
                .when().get(path, pathVariable)
                .then().log().all()
                .extract();
    }

    @Override
    @ValidateOk
    public ExtractableResponse<Response> update(String path, Map<String, Object> jsonBody) {
        return RestAssured.given().log().all()
                .body(jsonBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    @Override
    @ValidateNoContent
    public  <T> ExtractableResponse<Response> delete(String path, T pathVariable) {
        return RestAssured.given().log().all()
                .body(pathVariable)
                .when().delete(path, pathVariable)
                .then().log().all()
                .extract();
    }

}
