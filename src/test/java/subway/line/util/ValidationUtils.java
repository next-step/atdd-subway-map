package subway.line.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.util.CommonExtractionUtils;
import subway.line.MockLine;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static subway.line.LineApi.LINE_NAME_KEY;

public class ValidationUtils {

    private ValidationUtils() {}

    public static void checkLineExistence(ExtractableResponse<Response> response, MockLine line) {
        String lineNameOfResponse = CommonExtractionUtils.getNameOfKey(response, LINE_NAME_KEY);
        String lineName = line.getName();

        assertThat(lineNameOfResponse).isEqualTo(lineName);
    }

    public static void checkLineExistenceInList(ExtractableResponse<Response> response, MockLine line) {
        List<String> lineNamesOfResponse = CommonExtractionUtils.getNamesOfKey(response, LINE_NAME_KEY);
        String lineName = line.getName();

        assertTrue(lineNamesOfResponse.contains(lineName));
    }

    public static void checkLinesExistenceInList(ExtractableResponse<Response> response, MockLine line1, MockLine line2) {
        List<String> lineNamesOfResponse = CommonExtractionUtils.getNamesOfKey(response, LINE_NAME_KEY);
        List<String> lineNames = List.of(line1.getName(), line2.getName());

        assertTrue(lineNamesOfResponse.containsAll(lineNames));
    }
}
