package subway;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationUtils {

    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static void createStation(String name) {
        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(getRequestSpecification())
                        .body(Map.entry("name", name))
                        .when().post("/stations")
                        .then()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> selectStations() {
        return RestAssured
                .given().spec(StationUtils.getRequestSpecification())
                .when().get("/stations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> createStationLine(Map<String, Object> body) {
        return RestAssured.given().spec(getRequestSpecification()).body(body)
                .when().post("/lines")
                .then().extract();
    }

}
