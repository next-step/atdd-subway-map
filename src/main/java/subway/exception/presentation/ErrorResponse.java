package subway.exception.presentation;

public class ErrorResponse {
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse from(String message) {
       return new ErrorResponse(message);
    }

    public String getMessage() {
        return message;
    }
}
