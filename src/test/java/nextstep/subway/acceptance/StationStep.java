package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationStep {
    private StationStep() {
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/stations")
                          .then().log().all()
                          .extract();
    }

    public enum 지하철역_생성_수정_Params {
        강남역("강남역"),
        역삼역("역삼역");

        지하철역_생성_수정_Params(String name) {
            this.name = name;
        }

        private final String name;

        public String getName() {
            return name;
        }
    }
}
