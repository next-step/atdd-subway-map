package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AcceptanceTestBase {

    public static void assertStatusCode(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static Long getIdFromResponse(final ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    public static String getNameFromResponse(final ExtractableResponse<Response> response) {
        return response.jsonPath().getString("name");
    }

    public static List<String> getNamesFromResponse(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

}
