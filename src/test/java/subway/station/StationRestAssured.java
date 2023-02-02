package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class StationRestAssured {

    public List<ExtractableResponse<Response>> createStation(final String... stationNames) {
        return Arrays.stream(stationNames)
                .map(this::createStation)
                .collect(Collectors.toUnmodifiableList());
    }

    public ExtractableResponse<Response> createStation(final String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public ExtractableResponse<Response> deleteStation(final Long id) {
        return givenLog()
                .when()
                .delete("/stations/{id}", id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public ExtractableResponse<Response> findStations() {
        return givenLog()
                .when()
                .get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private RequestSpecification givenLog() {
        return RestAssured
                .given().log().all();
    }
}
