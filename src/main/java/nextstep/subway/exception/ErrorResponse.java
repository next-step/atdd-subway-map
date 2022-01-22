package nextstep.subway.exception;

public class ErrorResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse() {
    }
    public ErrorResponse(String message) {
        this.message = message;
    }
}
