package subway.dto;

public class ErrorResponse {

    private String message;

    protected ErrorResponse() {
    }

    public ErrorResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
