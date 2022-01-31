package nextstep.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.badRequest().build();
    }

}
