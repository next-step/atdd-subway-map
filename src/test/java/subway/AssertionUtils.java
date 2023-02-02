package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertionUtils {

    public static Executable withNames(ExtractableResponse<Response> response, String...names) {
        return () -> assertThat(response.jsonPath().getList("name", String.class)).containsAnyOf(names);
    }

    public static Executable withColors(ExtractableResponse<Response> response, String...colors) {
        return () -> assertThat(response.jsonPath().getList("color", String.class)).containsAnyOf(colors);
    }

    public static Executable withStationIdsInAnyOrder(ExtractableResponse<Response> response, Long...ids) {
        return () -> assertThat(response.jsonPath().getList("stations.id.flatten()", Long.class)).containsExactlyInAnyOrder(ids);
    }

    public static Executable withName(ExtractableResponse<Response> response, String name) {
        return () -> assertThat((String) response.jsonPath().get("name")).isEqualTo(name);
    }

    public static Executable withColor(ExtractableResponse<Response> response, String color) {
        return () -> assertThat((String) response.jsonPath().get("color")).isEqualTo(color);
    }

    public static void assertStationIdsInOrder(ExtractableResponse<Response> lineResponse, Long...ids) {
        assertThat(lineResponse.jsonPath().getList("stations.id.flatten()", Long.class)).containsExactly(ids);
    }
}
