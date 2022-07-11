package nextstep.subway.ui.config;

import nextstep.subway.domain.exception.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionResponse> domainExceptionHandler(DomainException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ExceptionResponse(exception.getMessage()));
    }
}
