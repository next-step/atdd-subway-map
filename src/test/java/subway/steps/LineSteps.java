package subway.steps;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LinePatchResponse;
import subway.dto.LineRequest;

import java.util.List;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, long upStationId, long downStationId, int distance) {
        LineRequest param = new LineRequest(name, color, upStationId, downStationId, distance);

        return RestAssured.given().log().all()
                .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static List<String> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all().extract().jsonPath().getList("name", String.class);
    }

    public static JsonPath 지하철_노선_단건_조회_요청(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when().get("/lines/{id}")
                .then().log().all().extract().jsonPath();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String name, String color, Long id) {
        LinePatchResponse patchResponse = new LinePatchResponse(name, color);
        return RestAssured.given().header("Content-type", "application/json")
                .log().all()
                .and()
                .body(patchResponse).pathParam("id", id)
                .when().patch("/lines/{id}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when().delete("/lines/{id}")
                .then().log().all().extract();
    }
}
