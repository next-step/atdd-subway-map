package subway.exception;

public class SubwayRestApiException extends RuntimeException {
    private final ErrorResponseCode errorResponse;

    public SubwayRestApiException(ErrorResponseCode errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponseCode getErrorResponse() {
        return errorResponse;
    }
}
