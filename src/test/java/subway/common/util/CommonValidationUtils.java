package subway.common.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonValidationUtils {

    public static void checkRequestCreated(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void checkResponseCount(ExtractableResponse<Response> response, int expected) {
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("")).hasSize(expected);
    }
}
