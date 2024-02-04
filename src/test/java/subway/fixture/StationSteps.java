package subway.fixture;

import io.restassured.RestAssured;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationResponse;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {

    public static StationResponse createStation(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);

        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(StationResponse.class);
    }
}
