package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayExceptionHandler {

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ResponseEntity<Void> handleIllegalStateException(IllegalArgumentException error) {
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(value = IllegalStateException.class)
  public ResponseEntity<Void> handleIllegalStateException(IllegalStateException error) {
    return ResponseEntity.badRequest().build();
  }
}
