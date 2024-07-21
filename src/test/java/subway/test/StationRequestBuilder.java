package subway.test;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationRequestBuilder {
    private final String name;

    private StationRequestBuilder(Builder builder) {
        this.name = builder.name;
    }

    public static StationResponseHelper requestGetAll() {
        var response = RestAssured
                .when().get("/stations")
                .then()
                .extract();
        return new StationResponseHelper(response);

    }

    public static StationResponseHelper requestDelete(Long id) {
        var response = RestAssured.given()
                .pathParam("id", id)
                .when()
                .delete("/stations/{id}")
                .then()
                .extract();
        return new StationResponseHelper(response);
    }

    public StationResponseHelper requestCreate() {
        var body = Map.of("name", this.name);
        var contentType = MediaType.APPLICATION_JSON_VALUE;
        var path = "/stations";

        var response = RestAssured.given().log().all()
                .body(body)
                .contentType(contentType)
                .when().post(path)
                .then().log().all()
                .extract();
        return new StationResponseHelper(response);
    }

    public static class Builder {
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public StationRequestBuilder build() {
            return new StationRequestBuilder(this);
        }
    }
}
