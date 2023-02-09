package subway.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

public class AssertUtil {

    public static void assertSuccessCreate(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    public static void assertSuccessOk(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    public static void assertSuccessNoContent(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    public static void assertFailBadRequest(ExtractableResponse<Response> response, String message) {
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(message);
    }

    public static void assertEqualToNames(List<String> names, String... expectedNames){
        assertThat(names.size()).isEqualTo(expectedNames.length);

        if (expectedNames.length > 0) {
            assertThat(names).containsExactly(expectedNames);
        }
    }

    public static void assertEqualToLine(JsonPath line, String expectedName, String expectedColor) {
        assertAll(
                () -> assertThat(line.getString("name")).isEqualTo(expectedName),
                () -> assertThat(line.getString("color")).isEqualTo(expectedColor)
        );
    }

    public static void assertEqualToSections(JsonPath sections, String[] expectedDownStationNames, String[] expectedUpStationNames, Integer... expectedDistance) {
        assertAll(
                () -> assertEqualToNames(sections.getList("sections.downStation.name", String.class), expectedDownStationNames),
                () -> assertEqualToNames(sections.getList("sections.upStation.name", String.class), expectedUpStationNames),
                () -> assertThat(sections.getList("sections.distance", Integer.class)).containsExactly(expectedDistance)
        );
    }
}
