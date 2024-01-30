package subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class Line {
    public static class Fixture {
        public Long id;
        public String name;
        public String color;
        public Station.Fixture upStation;
        public Station.Fixture downStation;
        public Long distance;

    }

    public static class Api {
        public static ExtractableResponse<Response> createLineBy(Line.Fixture line) {
            Map<String, String> params = new HashMap<>();
            params.put("name", line.name);
            params.put("color", line.color);
            params.put("upStationId", line.upStation.id.toString());
            params.put("downStationId", line.downStation.id.toString());
            params.put("distance", line.distance.toString());
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

        public static ExtractableResponse<Response> updateLineBy(Long id, String newName, String newColor) {
            Map<String, String> params = new HashMap<>();
            params.put("name", newName);
            params.put("color", newColor);

            return RestAssured
                    .given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().patch("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }

        public static ExtractableResponse<Response> deleteLineBy(Long id) {
            return RestAssured
                    .given().log().all()
                    .when().delete("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }
    }

    public static Line.Fixture 신분당선(Station.Fixture up, Station.Fixture down) {
        final String name = "신분당선";
        final String color = "bg-red-600";
        final Long distance = 10L;
        Line.Fixture line = new Line.Fixture();
        line.name = name;
        line.color = color;
        line.upStation = up;
        line.downStation = down;
        line.distance = distance;
        return line;
    }

    public static Line.Fixture 분당선(Station.Fixture up, Station.Fixture down) {
        final String name = "분당선";
        final String color = "bg-greed-600";
        final Long distance = 10L;

        Line.Fixture line = new Line.Fixture();
        line.name = name;
        line.color = color;
        line.upStation = up;
        line.downStation = down;
        line.distance = distance;
        return line;
    }
}
