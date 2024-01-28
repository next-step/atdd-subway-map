package subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.LineAcceptanceTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonApi {
    public static class Station {

        public static  ExtractableResponse<Response> createStationBy(String name) {
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            return RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();
        }

        public static  ExtractableResponse<Response> listStation() {
            return RestAssured.given().log().all()
                    .when().get("/stations")
                    .then().log().all()
                    .extract();
        }

        public static  List<String> listStationName() {
            return listStation().jsonPath().getList("name", String.class);
        }

        public static  ExtractableResponse<Response> deleteStationBy(Long id) {
            return  RestAssured
                    .given().log().all()
                    .when().delete("/stations/{id}", id)
                    .then().log().all()
                    .extract();
        }
    }
    public static class Line {

        public static ExtractableResponse<Response> createLineBy(Fixture.Line line) {
            Map<String, String> params = new HashMap<>();
            params.put("name", line.name);
            params.put("color", line.color);
            params.put("upStationId", line.upStationId);
            params.put("downStationId", line.downStationId);
            params.put("distance", line.distance);
            return RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines")
                    .then().log().all()
                    .extract();
        }

        public static ExtractableResponse<Response> listLine() {
            return RestAssured.given().log().all()
                    .when().get("/lines")
                    .then().log().all()
                    .extract();
        }

        public static ExtractableResponse<Response> retrieveLineBy(Long id) {
            return RestAssured.given().log().all()
                    .when().get("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }

        public static ExtractableResponse<Response> updateLineBy(Long id) {
            return RestAssured
                    .given().log().all()
                    .when().delete("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }

        public static ExtractableResponse<Response> deleteLineBy(Long id){
            return RestAssured
                    .given().log().all()
                    .when().delete("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }
    }


}
