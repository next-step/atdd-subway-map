package subway.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class DomainApiTest {

    public static ExtractableResponse<Response> 지하철노선을_생성한다(Map<String, Object> param) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_조회한다(int id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}",id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_수정한다(int id, Map<String, String> param1) {
        return RestAssured.given().log().all()
                .body(param1)
                .contentType(ContentType.JSON)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_삭제한다(int id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}",id)
                .then().log().all()
                .extract();
    }

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
