package subway.test;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineRequestBuilder {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    private LineRequestBuilder(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
    }

    public static LineResponseHelper requestGetAll() {
        var response = RestAssured
                .when().get("/lines")
                .then()
                .extract();
        return new LineResponseHelper(response);
    }

    public static LineResponseHelper requestGet(Long id) {
        var response = RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .get("/lines/{id}")
                .then()
                .extract();
        return new LineResponseHelper(response);
    }
    
    public static LineResponseHelper requestDelete(Long id) {
        var response = RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .delete("/lines/{id}")
                .then()
                .extract();
        return new LineResponseHelper(response);
    }

    public LineResponseHelper requestCreate() {
        var body = Map.of("name", this.name,
                "color", this.color,
                "upStationId", this.upStationId,
                "downStationId", this.downStationId,
                "distance", this.distance
        );
        var contentType = MediaType.APPLICATION_JSON_VALUE;
        var path = "/lines";

        var response = RestAssured.given().log().all()
                .body(body)
                .contentType(contentType)
                .when().post(path)
                .then().log().all()
                .extract();
        return new LineResponseHelper(response);

    }

    public LineResponseHelper requestUpdate(Long id) {
        var body = Map.of(
                "name", name,
                "color", color
        );
        var contentType = MediaType.APPLICATION_JSON_VALUE;

        var response = RestAssured
                .given()
                .pathParam("id", id)
                .body(body)
                .contentType(contentType)
                .when()
                .put("/lines/{id}")
                .then()
                .extract();

        return new LineResponseHelper(response);
    }

    public static class Builder {
        private String name;
        private String color;
        private Long distance;
        private Long upStationId;
        private Long downStationId;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public LineRequestBuilder build() {
            return new LineRequestBuilder(this);
        }
    }
}
