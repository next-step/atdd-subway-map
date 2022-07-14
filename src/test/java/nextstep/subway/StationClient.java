package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationClient {

    private static final String PATH = "/stations";

    public ExtractableResponse<Response> create(String stationName) {
        return RestAssured.given().log().all()
                .body(Map.of("name", stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(PATH)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> findAll() {
        return RestAssured.given().log().all()
                .when().get(PATH)
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> deleteById(long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(PATH + "/" + id)
                .then().log().all().extract();
    }

}
