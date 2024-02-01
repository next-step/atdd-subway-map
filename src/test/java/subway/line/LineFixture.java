package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

    default ExtractableResponse<Response> createLineByNameAndStation(String lineName, String upStationName, String downStationName) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-yellow-600");
        params.put("distance", 10);
        params.put("upStationId", createStationIdByName(upStationName).getId());
        params.put("downStationId", createStationIdByName(downStationName).getId());

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
