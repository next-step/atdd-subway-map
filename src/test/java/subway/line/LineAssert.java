package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class LineAssert {

    private final LineRestAssured lineRestAssured;

    public LineAssert() {
        this.lineRestAssured = new LineRestAssured();
    }

    public void assertCreateLine(
            final String name,
            final String color,
            final int distance
    ) {
        ExtractableResponse<Response> response = lineRestAssured.showLines();

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getList("name")).contains(name),
                () -> assertThat(jsonPath.getList("color")).contains(color),
                () -> assertThat(jsonPath.getList("distance")).contains(distance)
        );
    }

    public void assertShowLine(
            final String name,
            final String color,
            final int distance,
            final String location
    ) {
        ExtractableResponse<Response> response = lineRestAssured.requestGet(location);

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getString("name")).isEqualTo(name),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(color),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(distance)
        );
    }

    public void assertEditLine(
            final String location,
            final String name,
            final Long upStationId,
            final Long downStationId,
            final String color,
            final int distance
    ) {
        ExtractableResponse<Response> response = lineRestAssured.requestGet(location);

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getString("name")).isEqualTo(name),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(color),
                () -> assertThat(jsonPath.getList("stations.id", Long.class)).contains(upStationId, downStationId),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(distance)
        );
    }

    public void assertDeleteLine(final String location) {
        RestAssured.given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
