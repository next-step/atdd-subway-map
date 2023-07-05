package subway.fixture;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SubwayFixtures {

    public static void createSubwayStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/stations")
            .then().statusCode(HttpStatus.CREATED.value())
            .and().extract();
    }
}