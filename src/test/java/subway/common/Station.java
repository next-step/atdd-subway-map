package subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Station {

    public static class Fixture {
        public final Long id;
        public final String name;

        private Fixture(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Api {

        public static ExtractableResponse<Response> createStationBy(String name) {
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE).when().post("/stations").then().log().all().extract();
        }

        public static ExtractableResponse<Response> listStation() {
            return RestAssured.given().log().all().when().get("/stations").then().log().all().extract();
        }

        public static List<String> listStationName() {
            return listStation().jsonPath().getList("name", String.class);
        }

        public static ExtractableResponse<Response> deleteStationBy(Long id) {
            return RestAssured.given().log().all().when().delete("/stations/{id}", id).then().log().all().extract();
        }
    }

    public static Station.Fixture 지하철역() {
        final String name = "지하철역";
        Long id = Station.Api.createStationBy(name).jsonPath().getLong("id");
        return new Fixture(id, name);
    }

    public static Station.Fixture 새로운지하철역() {
        final String name = "새로운지하철역";
        Long id = Station.Api.createStationBy(name).jsonPath().getLong("id");
        return new Fixture(id, name);
    }

    public static Station.Fixture 또다른지하철역() {
        final String name = "또다른지하철역";
        Long id = Station.Api.createStationBy(name).jsonPath().getLong("id");
        return new Fixture(id, name);
    }
}
