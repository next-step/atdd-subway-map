package nextstep.subway;

import nextstep.subway.line.exception.NoResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoResourceException.class)
  public ResponseEntity handleNoResourceException(NoResourceException e) {
    return ResponseEntity.notFound().build();
  }
}
