package subway.fixture;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class SubwayFixtures {

    public static Response createSubwayStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/stations")
            .then().statusCode(HttpStatus.CREATED.value())
            .and().extract();

        return (Response) response.body();
    }
}