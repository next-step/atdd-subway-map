package subway.extracts;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public class ResponseExtracts {
    public static HttpStatus 상태코드(ExtractableResponse<Response> response) {
        return HttpStatus.valueOf(response.statusCode());
    }
}
