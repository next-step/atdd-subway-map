package nextstep.subway.common.exception;

import nextstep.subway.station.exception.NotMatchingStationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({ExistResourceException.class, NotMatchingStationException.class, NotRemoveResourceException.class})
    public ResponseEntity<Void> existResourceExceptionHandler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(NonExistResourceException.class)
    public ResponseEntity<Void> nonExistResourceExceptionHandler(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler()
    public ResponseEntity<Void> invalidJpaDataExceptionHandler(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

}
