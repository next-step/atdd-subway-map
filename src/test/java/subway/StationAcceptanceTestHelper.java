package subway;

import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

import static subway.utils.AcceptanceTestUtils.createResource;

class StationAcceptanceTestHelper {

    protected static final String STATIONS_RESOURCE_URL = "/stations";

    protected ValidatableResponse createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return createResource(STATIONS_RESOURCE_URL, params);
    }
}
