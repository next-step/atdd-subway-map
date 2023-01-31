package subway.line.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.MockLine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationUtils {

    private ValidationUtils() {}

    public static void checkLineExistence(ExtractableResponse<Response> response, MockLine line) {
        List<String> lineNamesOfResponse = ExtractionUtils.getLineNames(response);

        assertTrue(lineNamesOfResponse.contains(line.getName()));
    }
}
