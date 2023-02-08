package subway.given;

import io.restassured.response.*;
import org.springframework.http.*;

import java.util.*;

import static io.restassured.RestAssured.*;

public class GivenStationApi {

    public static final String STATIONS_PATH = "/stations";

    public static final String STATION_1 = "지하철역1";
    public static final String STATION_2 = "지하철역2";
    public static final String STATION_3 = "지하철역3";

    public static ExtractableResponse<Response> createStationApi(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATIONS_PATH)
                .then().log().all()
                .extract();
    }

}
