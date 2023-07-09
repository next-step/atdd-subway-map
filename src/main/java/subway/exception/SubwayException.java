package subway.exception;

import lombok.Getter;

public class SubwayException extends RuntimeException {

    private final ErrorResponse response;

    public SubwayException(final long code, final String message) {
        this.response = ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public String getMessage() {
        return this.response.getMessage();
    }

    public ErrorResponse getResponse() {
        return response;
    }


}
