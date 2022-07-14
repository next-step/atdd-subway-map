package nextstep.subway.exception;

public class ErrorResponse {
    private Integer statusCode;
    private String message;

    public ErrorResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
