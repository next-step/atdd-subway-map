package subway.feature;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;


public class StationFeature extends RestAssuredFeature{

    private static final String BASE_URL = "/stations";
    private static final String NAME_KEY = "name";

    public static ExtractableResponse<Response> callCreateStation(final String station){
        return callCreate(BASE_URL, Map.of(NAME_KEY, station));
    }

    public static ExtractableResponse<Response> callGetStations(){
        return callGet(BASE_URL);
    }

    public static ExtractableResponse<Response> deleteStation(final Long id) {
        return callDelete(BASE_URL + "/" + id);
    }
}