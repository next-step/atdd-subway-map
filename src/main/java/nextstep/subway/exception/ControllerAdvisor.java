package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = LogicException.class)
    public ResponseEntity<String> exception(LogicException exception) {
        return new ResponseEntity<>(
                exception.getLogicError().getError(),
                exception.getLogicError().getHttpStatus()
        );
    }
}
