package nextstep.subway.ui;

import nextstep.subway.exception.DuplicatedNameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class BaseController {

    @ExceptionHandler({DuplicatedNameException.class, NoSuchElementException.class})
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.badRequest().build();
    }

}
