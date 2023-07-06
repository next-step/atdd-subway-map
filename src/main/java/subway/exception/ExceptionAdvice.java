package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    private static final String DEFAULT_ERROR_MESSAGE = "시스템 에러가 발생했습니다.";

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity subwayExceptionHandler(final SubwayException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity ExceptionHandler() {
        return ResponseEntity.internalServerError().body(DEFAULT_ERROR_MESSAGE);
    }

}
