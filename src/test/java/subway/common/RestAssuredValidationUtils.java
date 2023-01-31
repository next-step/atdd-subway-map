package subway.common;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RestAssuredValidationUtils {
    public static void validateStatusCode(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static <T> T extractValue(ExtractableResponse<Response> response, String path, Class<T> clazz) {
        return response.jsonPath().getObject(path, clazz);
    }

    public static <T> void validateFieldEquals(ExtractableResponse<Response> response, String path, Class<T> clazz, T expected) {
        assertThat(extractValue(response, path, clazz)).isEqualTo(expected);
    }

    public static <T> void validateFieldContainsExactly(List<T> actual, T... expected) {
        assertThat(actual).containsExactly(expected);
    }

    public static <T> void validateFieldBodyHasSize(List<T> actual, int expected) {
        assertThat(actual).hasSize(expected);
    }
}
