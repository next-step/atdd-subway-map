package subway;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TestHelper {

    public static List<StationResponse> selectAllStations() {
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().
                            log().all()
                        .when()
                            .get("/stations")
                        .then()
                            .log().all()
                            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.body().as(new TypeRef<>() {});
    }

    public static StationResponse createStation(String name) {
        ExtractableResponse<Response> response =
                RestAssured.given()
                            .log().all()
                            .body(Map.of("name", name))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().
                            post("/stations")
                        .then()
                            .log().all()
                            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().as(StationResponse.class);
    }

    public static LineResponse createLine(String name) {
        ExtractableResponse<Response> response = RestAssured
                .given()
                    .log().all()
                    .body(Map.of("name", name, "color", "bg-red-600"))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .log().all()
                    .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().as(LineResponse.class);
    }

    public static List<LineResponse> selectAllLines() {
        ExtractableResponse<Response> response = RestAssured
                .given()
                    .log().all()
                .when()
                    .get("/lines")
                .then()
                    .log().all()
                    .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.body().as(new TypeRef<>() {});
    }

    public static LineResponse selectLine(long id) {
        ExtractableResponse<Response> response = RestAssured
                .given()
                    .log().all()
                .when()
                    .get("/lines/{id}", id)
                .then()
                    .log().all()
                    .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.body().as(LineResponse.class);
    }


}
