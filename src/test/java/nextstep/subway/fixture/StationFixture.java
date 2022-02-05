package nextstep.subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationFixture {

    public static final String 강남역_이름 = "강남역";
    public static final String 역삼역_이름 = "역삼역";

    public static ExtractableResponse<Response> 역_생성(String name) {
        Map<String, String> 역 = 역(name);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(역)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
        return response;
    }

    public static Map<String, String> 역(String name) {
        Map<String, String> 역 = new HashMap<>();
        역.put("name", name);
        return 역;
    }
}
