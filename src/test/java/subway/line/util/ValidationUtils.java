package subway.line.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.MockLine;
import subway.station.MockStation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationUtils {

    private ValidationUtils() {}

    public static void checkLineExistence(ExtractableResponse<Response> response, MockLine... lines) {
        List<String> lineNamesOfResponse = ExtractionUtils.getLineNames(response);
        List<String> lineNames = takeLineNames(lines);

        assertTrue(lineNamesOfResponse.containsAll(lineNames));
    }

    private static List<String> takeLineNames(MockLine[] lines) {
        return Arrays.stream(lines)
                .map(line -> line.getName())
                .collect(Collectors.toList());
    }
}
