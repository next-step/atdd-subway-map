package nextstep.subway.applicaion.exception;

import org.springframework.http.HttpStatus;

public class IllegalSectionException extends RuntimeException{

    public static final String MESSAGE = "제약을 준수하세요";
    private HttpStatus code = HttpStatus.BAD_REQUEST;

    public IllegalSectionException() {
        super(MESSAGE);
    }

    public HttpStatus getCode() {
        return code;
    }
}
