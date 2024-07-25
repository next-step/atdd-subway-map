package subway.test;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionRequestBuilder {
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    private SectionRequestBuilder(Builder builder) {
        this.downStationId = builder.downStationId;
        this.upStationId = builder.upStationId;
        this.distance = builder.distance;
    }

    public static SectionResponseHelper requestDelete(Long lineId, Long stationId) {
        var response = RestAssured
                .given()
                .pathParam("id", lineId)
                .queryParam("stationId", stationId)
                .when()
                .delete("/lines/{id}/sections")
                .then()
                .extract();
        return new SectionResponseHelper(response);
    }

    public SectionResponseHelper requestCreate(Long lineId) {
        var body = Map.of(
                "upStationId", this.upStationId,
                "downStationId", this.downStationId,
                "distance", this.distance
        );
        var contentType = MediaType.APPLICATION_JSON_VALUE;
        var path = "/lines/{lineId}/sections";

        var response = RestAssured.given().log().all()
                .body(body)
                .contentType(contentType)
                .pathParam("lineId", lineId)
                .when().post(path)
                .then().log().all()
                .extract();

        return new SectionResponseHelper(response);
    }

    public static class Builder {
        private Long upStationId;
        private Long downStationId;
        private Long distance;

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

        public SectionRequestBuilder build() {
            return new SectionRequestBuilder(this);
        }
    }
}
