package subway.exception;

public class SubwayIllegalArgumentException extends IllegalArgumentException {
    private final ErrorResponseCode errorResponseCode;

    public SubwayIllegalArgumentException(ErrorResponseCode errorResponseCode) {
        this.errorResponseCode = errorResponseCode;
    }

    public ErrorResponseCode getErrorResponseCode() {
        return errorResponseCode;
    }
}
