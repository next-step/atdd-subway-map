package subway.exception;

public class ExceptionResponse {

    String code;

    String message;

    public ExceptionResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
