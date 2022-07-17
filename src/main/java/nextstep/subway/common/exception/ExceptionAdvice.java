package nextstep.subway.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler(SectionException.class)
  public ResponseEntity<ExceptionResponse> sectionException(SectionException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ExceptionResponse.getInstance(e.getMessage()));
  }

  @ExceptionHandler(EntityNotFound.class)
  public ResponseEntity<ExceptionResponse> notFoundException(EntityNotFound e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ExceptionResponse.getInstance(e.getMessage()));
  }
}
