package subway.testhelper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class StationApiCaller {

    public ExtractableResponse<Response> callCreateStation(Map<String, String> params) {
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> callFindStations() {
        return given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
}
