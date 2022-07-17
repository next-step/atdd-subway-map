package nextstep.subway.exception;

public class ExceptionResponse {
    private String errorMessage;

    private ExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ExceptionResponse of(String errorMessage) {
        return new ExceptionResponse(errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
