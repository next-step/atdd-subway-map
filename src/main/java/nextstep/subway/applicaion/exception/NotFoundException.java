package nextstep.subway.applicaion.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException{

    public static final String MESSAGE = "찾는 개체가 없습니다.";
    private HttpStatus code = HttpStatus.NOT_FOUND;

    public NotFoundException() {
        super(MESSAGE);
    }

    public HttpStatus getCode() {
        return code;
    }
}
