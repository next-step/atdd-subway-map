package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RestUtil {

    public Long createEntityData(Map<String, Object> params, String requestUrl) {
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post(requestUrl)
                        .then().log().all()
                        .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return ((Number)response.jsonPath().get("id")).longValue();
    }
    public Long registEntityData(Map<String, Object> params, String requestUrl){
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post(requestUrl)
                        .then().log().all()
                        .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return ((Number)response.jsonPath().get("id")).longValue();
    }

    public ExtractableResponse<Response> getResponseData(String requestUrl){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(requestUrl)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> getResponseDataById(String requestUrl, Long id){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(requestUrl, id)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> deleteEntityDataById(String requestUrl, Long id){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(requestUrl, id)
                .then().log().all()
                .extract();
    }


}
