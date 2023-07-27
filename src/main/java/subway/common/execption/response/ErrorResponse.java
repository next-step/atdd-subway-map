package subway.common.execption.response;

public class ErrorResponse {

    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }
}
