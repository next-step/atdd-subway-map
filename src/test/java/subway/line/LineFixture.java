package subway.line;

import io.restassured.RestAssured;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.dto.response.StationResponse;

import java.util.HashMap;
import java.util.Map;

public interface LineFixture {

    default StationResponse createStationIdByName(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(StationResponse.class);
    }
}
