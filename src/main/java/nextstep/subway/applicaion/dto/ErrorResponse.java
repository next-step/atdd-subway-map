package nextstep.subway.applicaion.dto;

public class ErrorResponse {

    private String errorName;

    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorName, String message) {
        this.errorName = errorName;
        this.message = message;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getMessage() {
        return message;
    }
}
