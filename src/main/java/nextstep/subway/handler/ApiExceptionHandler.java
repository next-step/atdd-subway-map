package nextstep.subway.handler;

import javax.servlet.http.HttpServletRequest;
import nextstep.subway.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> notFound(final NotFoundException e, final HttpServletRequest request) {
        return ResponseEntity.notFound().build();
    }

}
