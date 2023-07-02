package subway;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


public final class AcceptanceTestUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static <T> String convertToJsonString(T requestBody) {
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void verifyResponseStatus(ValidatableResponse stationFoundResponse, HttpStatus status) {
        stationFoundResponse.assertThat().statusCode(status.value());
    }
    public static String getLocation(ValidatableResponse response) {
        return response.extract().header(HttpHeaders.LOCATION);
    }
}
