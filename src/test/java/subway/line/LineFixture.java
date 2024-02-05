package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public interface LineFixture {
    default ExtractableResponse<Response> createLineByNameAndStation(String lineName) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-yellow-600");

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
