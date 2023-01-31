package subway.station.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.util.CommonExtractionUtils;
import subway.common.util.CommonValidationUtils;
import subway.station.MockStation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static subway.station.StationApi.STATION_NAME_KEY;

public class ValidationUtils extends CommonValidationUtils {

    private ValidationUtils() {}


    public static void checkStationExistence(ExtractableResponse<Response> response, MockStation... stations) {
        List<String> stationNamesOfResponse = CommonExtractionUtils.getNamesOfKey(response, STATION_NAME_KEY);
        List<String> stationNames = takeStationNames(stations);

        assertTrue(stationNamesOfResponse.containsAll(stationNames));
    }

    public static void checkStationNotExistence(ExtractableResponse<Response> response, MockStation... stations) {
        List<String> stationNamesOfResponse = CommonExtractionUtils.getNamesOfKey(response, STATION_NAME_KEY);
        List<String> stationNames = takeStationNames(stations);

        assertFalse(stationNamesOfResponse.containsAll(stationNames));
    }

    private static List<String> takeStationNames(MockStation[] stations) {
        return Arrays.stream(stations)
                .map(station -> station.name())
                .collect(Collectors.toList());
    }
}
