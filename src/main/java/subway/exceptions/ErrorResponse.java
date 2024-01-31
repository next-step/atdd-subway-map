package subway.exceptions;

import java.util.List;

public class ErrorResponse {

    private String message;
    private int status;
    private List<FieldError> errors;
    private String code;

    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }

    public static ErrorResponse of(ErrorCode code) {
        final ErrorResponse response = new ErrorResponse();

        response.message = code.getMessage();
        response.status = code.getStatus();
        response.code = code.getCode();

        return response;
    }
}
