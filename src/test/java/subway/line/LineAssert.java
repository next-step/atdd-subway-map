package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class LineAssert {

    public void assertCreateLine(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getLong("id[0]")).isEqualTo(1),
                () -> assertThat(jsonPath.getList("name")).contains(name),
                () -> assertThat(jsonPath.getList("color")).contains(color),
                () -> assertThat(jsonPath.getList("distance")).contains(distance)
        );
    }

    public void assertShowLine(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance,
            final String location
    ) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

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
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getString("name")).isEqualTo(name),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(color),
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
