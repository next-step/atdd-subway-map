package nextstep.subway.applicaion.dto;

public class ExceptionResponse {

    private String message;
    private String error;

    public ExceptionResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
