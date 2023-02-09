package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

public class LineUtils {
    public static ExtractableResponse<Response> createLine(Map<String, String> params) {
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        return response;

    }

    public static List<String> getLineNames() {
        List<String> lineNames = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        return lineNames;
    }

    public static String getLineName(Long id) {
        String lineName = RestAssured.given().log().all()
                .when().get("/lines/" + String.valueOf(id))
                .then().log().all()
                .extract().jsonPath().getString("name");
        return lineName;
    }

    public static ExtractableResponse<Response> updateLine(Map<String, String> updateParams, Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log(). all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateParams)
                .when().put("/lines/" + String.valueOf(id))
                .then().log().all()
                .extract();
        return response;
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/" + String.valueOf(id))
                .then().log().all()
                .extract();
        return response;
    }

}
