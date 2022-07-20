package nextstep.subway.applicaion.exception;

public class ExceptionResponse {
    String message;


    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
