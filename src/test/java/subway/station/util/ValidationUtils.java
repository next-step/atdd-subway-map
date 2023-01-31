package subway.station.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import subway.station.MockStation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationUtils {

    private ValidationUtils() {}

    public static void checkStationCreated(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void checkStationExistence(ExtractableResponse<Response> response, MockStation... stations) {
        List<String> stationNamesOfResponse = ExtractionUtils.getStationNames(response);
        List<String> stationNames = takeStationNames(stations);

        assertTrue(stationNamesOfResponse.containsAll(stationNames));
    }

    public static void checkStationNotExistence(ExtractableResponse<Response> response, MockStation... stations) {
        List<String> stationNamesOfResponse = ExtractionUtils.getStationNames(response);
        List<String> stationNames = takeStationNames(stations);

        assertFalse(stationNamesOfResponse.containsAll(stationNames));
    }

    private static List<String> takeStationNames(MockStation[] stations) {
        return Arrays.stream(stations)
                .map(station -> station.name())
                .collect(Collectors.toList());
    }

    public static void checkStationCount(ExtractableResponse<Response> response, int expected) {
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("")).hasSize(expected);
    }
}
