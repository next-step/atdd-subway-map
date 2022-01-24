package nextstep.subway.acceptance.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class LineSteps {

    private LineSteps() {}

    public static ExtractableResponse<Response> 지하철노선_생성요청(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철노선_단건조회(Long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all().extract();
    }
}
