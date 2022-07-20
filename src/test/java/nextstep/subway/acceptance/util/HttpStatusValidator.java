package nextstep.subway.acceptance.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class HttpStatusValidator {

    public ExtractableResponse<Response> validateCreated(ExtractableResponse<Response> response) {
        validateStatusCode(response, HttpStatus.CREATED);
        return response;
    }

    public ExtractableResponse<Response> validateOk(ExtractableResponse<Response> response) {
        validateStatusCode(response, HttpStatus.OK);
        return response;
    }

    public ExtractableResponse<Response> validateNoContent(ExtractableResponse<Response> response) {
        validateStatusCode(response, HttpStatus.NO_CONTENT);
        return response;
    }

    public ExtractableResponse<Response> validateBadRequest(ExtractableResponse<Response> response) {
        validateStatusCode(response, HttpStatus.BAD_REQUEST);
        return response;
    }


    private void validateStatusCode(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

}
