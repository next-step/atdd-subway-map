package subway.common.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import subway.common.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonValidationUtils {

    public static final String MOCK_NAME_KEY = "name";

    private CommonValidationUtils() {}

    public static void checkCreatedResponse(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void checkDeletedResponse(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

    public static void checkNameExistence(ExtractableResponse<Response> response, Mock mock) {
        String nameOfResponse = CommonExtractionUtils.getValueOfKey(response, MOCK_NAME_KEY);
        String name = mock.getName();

        assertThat(nameOfResponse).isEqualTo(name);
    }

    public static void checkNameExistenceInList(ExtractableResponse<Response> response, Mock mock) {
        List<String> namesOfResponse = CommonExtractionUtils.getValuesOfKey(response, MOCK_NAME_KEY);
        String name = mock.getName();

        assertTrue(namesOfResponse.contains(name));
    }

    public static void checkNamesNotExistenceInList(ExtractableResponse<Response> response, Mock mock) {
        List<String> namesOfResponse = CommonExtractionUtils.getValuesOfKey(response, MOCK_NAME_KEY);
        String name = mock.getName();

        assertFalse(namesOfResponse.contains(name));
    }

    public static void checkNamesExistenceInList(ExtractableResponse<Response> response, Mock mock1, Mock mock2) {
        List<String> namesOfResponse = CommonExtractionUtils.getValuesOfKey(response, MOCK_NAME_KEY);
        List<String> names = List.of(mock1.getName(), mock2.getName());

        assertTrue(namesOfResponse.containsAll(names));
    }
}