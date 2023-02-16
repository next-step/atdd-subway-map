package subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationSteps {

    public static Long 지하철_역_생성_요청(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        return RestAssured.given().log().all()
                .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all().extract().jsonPath().getLong("id");
    }

    public static List<String> 지하철_역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all().extract()
                .jsonPath().getList("name", String.class);
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when().delete("/stations/{id}")
                .then().log().all().extract();
    }
}
