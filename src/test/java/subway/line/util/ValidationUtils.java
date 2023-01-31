package subway.line.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.util.CommonExtractionUtils;
import subway.line.MockLine;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static subway.line.LineApi.LINE_COLOR_KEY;

public class ValidationUtils {

    private ValidationUtils() {}

    public static void checkColorExistenceInList(ExtractableResponse<Response> response, MockLine line) {
        String colorsOfResponse = CommonExtractionUtils.getValueOfKey(response, LINE_COLOR_KEY);
        String color = line.getColor();

        assertTrue(colorsOfResponse.contains(color));
    }
}
