package subway.exception;

import lombok.Builder;

@Builder
public class ExceptionResponse {
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
