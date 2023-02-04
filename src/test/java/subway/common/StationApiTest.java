package subway.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class StationApiTest {
    public static ExtractableResponse<Response> 지하철역을_조회한다() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역을_생성한다(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역을_삭제한다(Map<String, String> deleteStation) {
        return RestAssured.given().log().all()
                .when().delete("/stations/{id}", deleteStation.get("id"))
                .then().statusCode(HttpStatus.NO_CONTENT.value())
                .log().all()
                .extract();
    }
}
