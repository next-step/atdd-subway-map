package fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.Station;

public class StationFixture {

    private StationFixture() {
    }

    public static Station giveOne(String name) {
        return Station.builder()
            .name(name)
            .build();
    }


    public static ExtractableResponse<Response> saveStationResponse(String name) {
        return RestAssured.given()
            .body(Map.of("name", name))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

}
