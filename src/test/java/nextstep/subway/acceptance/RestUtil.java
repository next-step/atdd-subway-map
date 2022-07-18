package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class RestUtil {

    public Long createEntityData(String requestUrl, int expectHttpStatusCode, Map<String, Object> params) {
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post(requestUrl)
                        .then().log().all()
                        .extract();
        assertThat(response.statusCode()).isEqualTo(expectHttpStatusCode);
        if(expectHttpStatusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            return -1L;
        }
        return ((Number)response.jsonPath().get("id")).longValue();
    }
    public Long registEntityData(String requestUrl, Map<String, Object> params){
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
    public <T> List<T> getResponseJsonListDataByKey(String requestUrl, String key, Class<T> klazz){

        return getResponseData(requestUrl)
                .jsonPath().getList(key,klazz)
                .stream().collect(Collectors.toList());
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

    public ExtractableResponse<Response> deleteEntityDataByIdWithQueryParam(String requestUrl, Long id, String key, Long value){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .given().queryParam(key, value)
                .when().delete(requestUrl, id)
                .then().log().all()
                .extract();
    }


}
