package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.common.StationLineNotFoundException;
import subway.common.StationNotFoundException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = {StationNotFoundException.class, StationLineNotFoundException.class})
    public ResponseEntity<Void> notFound(Exception ex) {
        return ResponseEntity.notFound().build();
    }
}
