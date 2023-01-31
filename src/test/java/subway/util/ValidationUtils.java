package subway.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import subway.MockStation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static subway.StationApi.STATION_NAME_KEY;

public class ValidationUtils {

    public static void checkStationCreated(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void checkStationExistence(ExtractableResponse<Response> response, MockStation... stations) {
        JsonPath jsonPath = response.jsonPath();
        for (MockStation station : stations) {
            assertTrue(jsonPath.getList(STATION_NAME_KEY).contains(station.name()));
        }
    }
    public static void checkStationNotExistence(ExtractableResponse<Response> response, MockStation... stations) {
        JsonPath jsonPath = response.jsonPath();
        for (MockStation station : stations) {
            assertFalse(jsonPath.getList(STATION_NAME_KEY).contains(station.name()));
        }
    }

    public static void checkStationCount(ExtractableResponse<Response> response, int expected) {
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("")).hasSize(expected);
    }
}
