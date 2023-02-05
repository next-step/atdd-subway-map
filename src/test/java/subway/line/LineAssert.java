package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineRestAssured.Location_조회;
import static subway.line.LineRestAssured.노선_목록_조회;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class LineAssert {

    public static void 노선_생성_검증(
            final Long id,
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        var response = 노선_목록_조회();

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getList("id", Long.class)).contains(id),
                () -> assertThat(jsonPath.getList("name")).contains(name),
                () -> assertThat(jsonPath.getList("color")).contains(color),
                () -> assertThat(jsonPath.getList("stations[0].id", Long.class)).contains(upStationId, downStationId),
                () -> assertThat(jsonPath.getList("distance")).contains(distance)
        );
    }

    public static void 노선_목록_조회_검증(final ExtractableResponse<Response> response, final int size) {
        assertThat(response.jsonPath().getList("id", Long.class)).hasSize(size);
    }

    public static void 노선_조회_검증(
            final String name,
            final String color,
            final int distance,
            final String location
    ) {
        ExtractableResponse<Response> response = Location_조회(location);

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getString("name")).isEqualTo(name),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(color),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(distance)
        );
    }

    public static void 노선_수정_검증(
            final String location,
            final String name,
            final Long upStationId,
            final Long downStationId,
            final String color,
            final int distance
    ) {
        ExtractableResponse<Response> response = Location_조회(location);

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(jsonPath.getString("name")).isEqualTo(name),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(color),
                () -> assertThat(jsonPath.getList("stations.id", Long.class)).contains(upStationId, downStationId),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(distance)
        );
    }

    public static void 노선_삭제_검증(final String location) {
        RestAssured.given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
