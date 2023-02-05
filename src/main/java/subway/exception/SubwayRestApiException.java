package subway.exception;

import subway.common.ErrorResponse;

public class SubwayRestApiException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public SubwayRestApiException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
