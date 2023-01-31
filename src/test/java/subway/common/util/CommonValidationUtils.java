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

    /**
     * List에 담기는 JSON의 개수를 검증한다.
     * 즉, 2개 이상의 반환값이 기대될 때만 사용해야 한다.
     *
     * @param response
     * @param expected
     */
    public static void checkResponseCount(ExtractableResponse<Response> response, int expected) {
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("")).hasSize(expected);
    }
}
