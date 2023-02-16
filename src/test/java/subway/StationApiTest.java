package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.domain.Station;

import java.util.List;

public class StationApiTest {
    static ExtractableResponse<Response> createStation(String stationName){
        Station station = new Station(stationName);
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(station)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();
        return response;
    }

    static List<String> getStationNames(){
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        return stationNames;
    }

    static ExtractableResponse<Response> deleteStation(ExtractableResponse<Response> response){
        int id = response.body().jsonPath().getInt("id");
        return  RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }
}
